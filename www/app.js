const $ = id => document.getElementById(id);

function setStatus(text) {
  $("status").textContent = text;
}

function callNative(action, args = [], success = () => {}, error = e => setStatus("ERROR: " + e)) {
  cordova.exec(success, error, "IfeSQLite", action, args);
}

function loadRecords() {
  callNative("getAll", [], rows => {
    const list = $("records");
    list.innerHTML = "";
    rows.forEach(row => {
      const li = document.createElement("li");
      li.textContent = row.id + ": " + row.name;
      list.appendChild(li);
    });
    setStatus(rows.length + " record(s) loaded from ife-shop.db");
  });
}

function saveRecord() {
  const name = $("name").value.trim();
  if (!name) {
    setStatus("Enter a name first.");
    return;
  }

  callNative("insert", [name], id => {
    $("name").value = "";
    setStatus("Saved record ID " + id + " to ife-shop.db");
    loadRecords();
  });
}

function clearRecords() {
  callNative("clear", [], count => {
    setStatus(count + " record(s) deleted.");
    loadRecords();
  });
}

document.addEventListener("deviceready", () => {
  setStatus("Java SQLite ready. Database: ife-shop.db");
  $("save").addEventListener("click", saveRecord);
  $("refresh").addEventListener("click", loadRecords);
  $("clear").addEventListener("click", clearRecords);
  $("name").addEventListener("keydown", e => {
    if (e.key === "Enter") saveRecord();
  });
  loadRecords();
});
