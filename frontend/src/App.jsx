
import { useCallback, useEffect, useMemo, useState,useRef } from "react";

import EmployeeForm from "./components/EmployeeForm";
import {
  getEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
} from "./services/employeeService";

import "./App.css";

function getInitials(employee) {
  return `${employee.firstName?.[0] ?? ""}${
    employee.lastName?.[0] ?? ""
  }`.toUpperCase();
}

function App() {
  const [employees, setEmployees] = useState([]);
  const [search, setSearch] = useState("");
  const [editingEmployee, setEditingEmployee] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  /*const renderCount =useRef(0);
  renderCount.current+=1;
  console.log(`Render #${renderCount.current}`,{loading,employees:employees.length,showForm});
  useEffect(()=>{
    console.log(`Render #${renderCount.current} is now om screen`);
    debugger;
  });*/
  let count=0
  console.log(count++);
  const loadEmployees = useCallback(async () => {
    setLoading(true);
    setError("");

    try {
      const data = await getEmployees();
      setEmployees(data);
    } catch (err) {
      setError(err.message || "Could not load employees.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadEmployees();
  }, [loadEmployees]);

  const filteredEmployees = useMemo(() => {
    const query = search.trim().toLowerCase();

    if (!query) return employees;

    return employees.filter((employee) =>
      [
        employee.firstName,
        employee.lastName,
        employee.email,
        employee.department,
        employee.designation,
      ].some((value) => value?.toLowerCase().includes(query))
    );
  }, [employees, search]);

  const departmentCount = useMemo(() => {
    return new Set(
      employees
        .map((employee) => employee.department)
        .filter(Boolean)
    ).size;
  }, [employees]);

  function openAddForm() {
    setEditingEmployee(null);
    setNotice("");
    setShowForm(true);
  }

  function openEditForm(employee) {
    setEditingEmployee(employee);
    setNotice("");
    setShowForm(true);
  }

  async function handleSave(employeeData) {
    setSaving(true);
    setError("");
    setNotice("");

    console.log("handleSubmit clicked redirected to handleSave then if we get count then ,which means after it ,component is rendered");

    try {
      if (editingEmployee) {
        await updateEmployee(editingEmployee.id, employeeData);
        setNotice("Employee updated successfully.");
      } else {
        await createEmployee(employeeData);
        setNotice("Employee added successfully.");
      }

      setShowForm(false);
      setEditingEmployee(null);
      await loadEmployees();
    } catch (err) {
      setError(err.message || "Could not save employee.");
      throw err;
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(employee) {
    const confirmed = window.confirm(
      `Delete ${employee.firstName} ${employee.lastName}?`
    );

    if (!confirmed) return;

    setError("");
    setNotice("");

    try {
      await deleteEmployee(employee.id);
      setEmployees((previous) =>
        previous.filter((item) => item.id !== employee.id)
      );
      setNotice("Employee deleted successfully.");
    } catch (err) {
      setError(err.message || "Could not delete employee.");
    }
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-icon">E</div>
          <div>
            <h2>EmployEase</h2>
            <span>Management Portal</span>
          </div>
        </div>

        <p className="nav-label">WORKSPACE</p>

        <div className="nav-item active">
          <span>▦</span> Dashboard
        </div>

        <div className="nav-item">
          <span>♙</span> Employees
        </div>

        <div className="sidebar-bottom">
          <div className="avatar">AD</div>
          <div>
            <strong>Administrator</strong>
            <span>Local workspace</span>
          </div>
        </div>
      </aside>

      <main className="main-content">
        <header className="topbar">
          <div>
            <p className="eyebrow">WORKSPACE / OVERVIEW</p>
            <h1>Employee Dashboard</h1>
          </div>
          
          <button className="button primary" onClick={openAddForm}>
            + Add Employee
          </button>
        </header>

        <section className="welcome-banner">
          <div>
            <p className="eyebrow">PEOPLE OPERATIONS</p>
            <h2>Manage your team in one place.</h2>
            <p>
              Keep employee information organized and up to date.
            </p>
          </div>
          <div className="welcome-symbol">✳</div>
        </section>

        <section className="stats-grid">
          <article className="stat-card">
            <div className="stat-top">
              <span>Total employees</span>
              <span className="stat-icon">♙</span>
            </div>
            <strong>{loading ? "—" : employees.length}</strong>
            <p>Employee records in the system</p>
          </article>

          <article className="stat-card">
            <div className="stat-top">
              <span>Departments</span>
              <span className="stat-icon purple">▦</span>
            </div>
            <strong>{loading ? "—" : departmentCount}</strong>
            <p>Distinct departments represented</p>
          </article>

          <article className="stat-card">
            <div className="stat-top">
              <span>Search results</span>
              <span className="stat-icon green">⌕</span>
            </div>
            <strong>{loading ? "—" : filteredEmployees.length}</strong>
            <p>Employees matching your search</p>
          </article>
        </section>

        {notice && (
          <div className="notice-message" role="status">
            {notice}
          </div>
        )}

        {error && (
          <div className="error-banner" role="alert">
            <span>{error}</span>
            <button className="text-button" onClick={loadEmployees}>
              Retry
            </button>
          </div>
        )}

        {showForm && (
          <div className="modal-backdrop">
            <div
              className="modal-panel"
              role="dialog"
              aria-modal="true"
              aria-labelledby="employee-form-title"
            >
              <EmployeeForm
                key={editingEmployee?.id ?? "new"}
                employee={editingEmployee}
                onSave={handleSave}
                onCancel={() => {
                  setShowForm(false);
                  setEditingEmployee(null);
                }}
                saving={saving}
              />
            </div>
          </div>
        )}

        <section className="employee-panel">
          <div className="panel-heading">
            <div>
              <h2>All employees</h2>
              <p>View and manage your employee directory.</p>
            </div>

            <button
              className="button secondary"
              onClick={loadEmployees}
              disabled={loading}
            >
              ↻ Refresh
            </button>
          </div>

          <div className="table-toolbar">
            <input
              className="search-input"
              type="search"
              placeholder="Search by name, email, department..."
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              aria-label="Search employees"
            />

            <span className="result-count">
              {filteredEmployees.length} records
            </span>
          </div>

          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>EMPLOYEE</th>
                  <th>DEPARTMENT</th>
                  <th>DESIGNATION</th>
                  <th>SALARY</th>
                  <th>ACTIONS</th>
                </tr>
              </thead>

              <tbody>
                {loading ? (
                  <tr>
                    <td colSpan="5" className="empty-state">
                      Loading employees...
                    </td>
                  </tr>
                ) : filteredEmployees.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="empty-state">
                      {search
                        ? "No employees match your search."
                        : "No employees found. Add your first employee."}
                    </td>
                  </tr>
                ) : (
                  filteredEmployees.map((employee) => (
                    <tr key={employee.id}>
                      <td>
                        <div className="employee-cell">
                          <div className="avatar employee-avatar">
                            {getInitials(employee)}
                          </div>
                          <div>
                            <strong>
                              {employee.firstName} {employee.lastName}
                            </strong>
                            <span>{employee.email}</span>
                          </div>
                        </div>
                      </td>

                      <td>
                        <span className="department-badge">
                          {employee.department || "Unassigned"}
                        </span>
                      </td>

                      <td>{employee.designation || "—"}</td>

                      <td>
                        {employee.salary == null
                          ? "—"
                          : Number(employee.salary).toLocaleString(
                              "en-IN",
                              {
                                style: "currency",
                                currency: "INR",
                                maximumFractionDigits: 2,
                              }
                            )}
                      </td>

                      <td>
                        <div className="action-buttons">
                          <button
                            className="text-button"
                            onClick={() => openEditForm(employee)}
                          >
                            Edit
                          </button>
                          <button
                            className="text-button delete-button"
                            onClick={() => handleDelete(employee)}
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </section>

        <footer className="footer">
          Employee Management System <span>•</span> Development environment
        </footer>
      </main>
    </div>
  );
}

export default App;