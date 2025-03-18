import { useState } from "react";
import PropTypes from "prop-types"; // Import PropTypes
import axios from "axios";

const Login = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null); // State for error messages

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null); // Clear previous errors
    try {
      const response = await axios.post(
        "http://localhost:8080/auth/login",
        null,
        {
          params: { username, password },
        }
      );

      if (response.data === "Login successful!") {
        alert("Login successful!"); // Optional
        onLoginSuccess(); // Proceed to the Ticket Management System
      } else {
        setError("Invalid username or password."); // Handle invalid login response
      }
    } catch (error) {
      console.error("Login error:", error);
      setError("Login failed. Please check your connection or credentials.");
    }
  };

  return (
    <form onSubmit={handleLogin}>
      <h2>Login</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}{" "}
      {/* Display error message */}
      <div>
        <label>Username:</label>
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          placeholder="Enter your username"
        />
      </div>
      <div>
        <label>Password:</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          placeholder="Enter your password"
        />
      </div>
      <button type="submit" style={{ marginTop: "10px" }}>
        Login
      </button>
    </form>
  );
};

// Add PropTypes validation
Login.propTypes = {
  onLoginSuccess: PropTypes.func.isRequired, // Validate that onLoginSuccess is a required function
};

export default Login;
