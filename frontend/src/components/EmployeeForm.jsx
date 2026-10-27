
import { useEffect, useState } from "react";

const emptyForm = {
  firstName: "",
  lastName: "",
  email: "",
  department: "",
  designation: "",
  salary: "",
};

function EmployeeForm({ employee, onSave, onCancel, saving }) {
  const [form, setForm] = useState(emptyForm);
  const [error, setError] = useState("");

  useEffect(() => {
    if (employee) {
      setForm({
        firstName: employee.firstName ?? "",
        lastName: employee.lastName ?? "",
        email: employee.email ?? "",
        department: employee.department ?? "",
        designation: employee.designation ?? "",
        salary: employee.salary ?? "",
      }); 
    } else {
      setForm(emptyForm);
    }

    setError("");
  }, [employee]);

  function handleChange(event) {
    const { name, value } = event.target;
    console.log("typed",name,value);
   

    setForm((previous) => {
      console.log("previous: ",previous);

      const next=
      {
      ...previous,
      [name]: value,
      };
      console.log("next",next);
      return next;
        });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");

    try {
      await onSave({
        ...form,
        salary:
          form.salary === "" ? null : Number(form.salary),
      });
    } catch (err) {
      setError(err.message || "Unable to save employee.");
    }
  }

  return (
    <form className="employee-form" onSubmit={handleSubmit}>
      <div className="form-heading">
        <h2>{employee ? "Edit employee" : "Add employee"}</h2>
        <p>
          {employee
            ? "Update the employee details below."
            : "Enter the employee details below."}
        </p>
      </div>

      <div className="form-grid">
        <label>
          First name
          <input
            name="firstName"
            value={form.firstName}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Last name
          <input
            name="lastName"
            value={form.lastName}
            onChange={handleChange}
            required
          />
        </label>

        <label className="full-width">
          Email address
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Department
          <select
            name="department"
            value={form.department}
            onChange={handleChange}
          >
            <option value="">Select department</option>
            <option value="IT">IT</option>
            <option value="HR">HR</option>
            <option value="Finance">Finance</option>
            <option value="Marketing">Marketing</option>
            <option value="Operations">Operations</option>
          </select>
        </label>

        <label>
          Designation
          <input
            name="designation"
            value={form.designation}
            onChange={handleChange}
            placeholder="Software Developer"
          />
        </label>

        <label className="full-width">
          Annual salary
          <input
            type="number"
            name="salary"
            value={form.salary}
            onChange={handleChange}
            min="0"
            step="0.01"
          />
        </label>
      </div>

      {error && <p className="error-message">{error}</p>}

      <div className="form-actions">
        <button
          type="button"
          className="button secondary"
          onClick={onCancel}
          disabled={saving}
        >
          Cancel
        </button>

        <button
          type="submit"
          className="button primary"
          disabled={saving}
        >
          {saving
            ? "Saving..."
            : employee
              ? "Save changes"
              : "Add employee"}
        </button>
      </div>
    </form>
  );
}

export default EmployeeForm;