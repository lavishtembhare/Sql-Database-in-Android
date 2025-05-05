# Sql-Database-in-Android

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>SQLite Contact CRUD App - Android</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 40px;
      line-height: 1.6;
      background-color: #f9f9f9;
      color: #333;
    }
    h1, h2, h3 {
      color: #2c3e50;
    }
    code {
      background-color: #eee;
      padding: 2px 4px;
      border-radius: 3px;
    }
    pre {
      background: #eee;
      padding: 10px;
      overflow-x: auto;
      border-radius: 5px;
    }
    .screenshot {
      display: flex;
      gap: 20px;
    }
    .screenshot img {
      max-width: 300px;
      border: 1px solid #ccc;
      border-radius: 6px;
    }
    ul {
      margin-left: 20px;
    }
  </style>
</head>
<body>

  <h1>📇 Android SQLite Contact CRUD App</h1>

  <p>This is a simple Android application built with Java and XML that demonstrates basic <strong>CRUD (Create, Read, Update, Delete)</strong> operations on a SQLite database to manage a list of contacts.</p>

  <h2>✨ Features</h2>
  <ul>
    <li>Add new contact (Name and Phone)</li>
    <li>Edit existing contact</li>
    <li>Delete a contact</li>
    <li>View list of all contacts</li>
    <li>Uses SQLite for local data storage</li>
    <li>Clean Material Design UI with RecyclerView and FloatingActionButton</li>
  </ul>

  <h2>🧱 Tech Stack</h2>
  <ul>
    <li>Java</li>
    <li>XML (Android UI)</li>
    <li>SQLite (local database)</li>
    <li>RecyclerView</li>
    <li>Material Components</li>
  </ul>

  <h2>🖼️ Screenshots</h2>
  <div class="screenshot">
    <div>
      <strong>Contact List</strong><br>
      <img src="screenshots/contact_list.png" alt="Contact List">
    </div>
    <div>
      <strong>Add/Edit Contact</strong><br>
      <img src="screenshots/add_edit_contact.png" alt="Add/Edit Contact">
    </div>
  </div>

  <h2>🚀 Getting Started</h2>

  <h3>Prerequisites</h3>
  <ul>
    <li>Android Studio</li>
    <li>Minimum SDK 21 (Android 5.0)</li>
  </ul>

  <h3>Installation</h3>
  <pre><code>git clone https://github.com/yourusername/sqlite-contact-crud.git</code></pre>
  <ol>
    <li>Open the project in <strong>Android Studio</strong></li>
    <li>Click <strong>Run ▶️</strong> on your emulator or Android device</li>
  </ol>

  <h2>📂 Project Structure</h2>
  <pre><code>
├── MainActivity.java
├── AddEditContactActivity.java
├── ContactAdapter.java
├── DatabaseHelper.java
├── models/
│   └── Contact.java
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── activity_add_edit_contact.xml
│   │   └── contact_item.xml
  </code></pre>

  <h2>📋 Usage</h2>
  <ul>
    <li>Tap the ➕ FAB to add a contact</li>
    <li>Tap a contact to edit</li>
    <li>Long-press a contact to delete</li>
  </ul>

  <h2>🛠️ TODO</h2>
  <ul>
    <li>Add search/filter functionality</li>
    <li>Use Room database instead of raw SQLite</li>
    <li>Add photo/avatar per contact</li>
  </ul>

  <h2>📄 License</h2>
  <p>This project is licensed under the MIT License - see the <code>LICENSE</code> file for details.</p>

</body>
</html>
