import { useState, useEffect } from "react";
import axios from "axios";
import Login from "./components/Login";
import ConfigurationForm from "./components/ConfigurationForm";
import ControlPanel from "./components/ControlPanel";
import LogDisplay from "./components/LogDisplay";
import "./App.css"; // Import the new styles

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isConfigured, setIsConfigured] = useState(false);
  const [logs, setLogs] = useState([]);
  const [isRunning, setIsRunning] = useState(false);

  const handleLoginSuccess = () => {
    setIsLoggedIn(true);
  };

  const handleConfigSubmit = (config) => {
    axios
      .post("http://localhost:8080/api/tickets/configuration", config)
      .then(() => {
        setLogs((prevLogs) => [
          ...prevLogs,
          `Configuration saved: Total Tickets=${config.totalTickets}, Max Capacity=${config.maxCapacity}, Ticket Release Rate=${config.releaseRate}, Customer Retrieval Rate=${config.retrievalRate}`,
        ]);
        setIsConfigured(true);
      })
      .catch((error) => {
        setLogs((prevLogs) => [...prevLogs, "Error saving configuration."]);
        console.error("Error saving configuration:", error);
      });
  };

  useEffect(() => {
    let intervalId;
    if (isRunning) {
      intervalId = setInterval(() => {
        axios.get("http://localhost:8080/api/tickets/logs").then((response) => {
          setLogs(response.data);
        });
      }, 1000);
    }

    return () => clearInterval(intervalId);
  }, [isRunning]);

  const handleStart = () => {
    axios
      .post("http://localhost:8080/api/tickets/start")
      .then(() => {
        setIsRunning(true);
        setLogs((prevLogs) => [...prevLogs, "System started."]);
      })
      .catch((error) => {
        setLogs((prevLogs) => [...prevLogs, "Error starting the system."]);
        console.error("Error starting the system:", error);
      });
  };

  const handleStop = () => {
    axios
      .post("http://localhost:8080/api/tickets/stop")
      .then(() => {
        setIsRunning(false);
        setLogs((prevLogs) => [...prevLogs, "System stopped."]);
      })
      .catch((error) => {
        setLogs((prevLogs) => [...prevLogs, "Error stopping the system."]);
        console.error("Error stopping the system:", error);
      });
  };

  return (
    <div>
      {!isLoggedIn ? (
        <Login onLoginSuccess={handleLoginSuccess} />
      ) : !isConfigured ? (
        <ConfigurationForm onSubmit={handleConfigSubmit} />
      ) : (
        <>
          <h1>Ticket Management System</h1>
          <div className="control-panel">
            <ControlPanel
              onStart={handleStart}
              onStop={handleStop}
              isRunning={isRunning}
            />
          </div>
          <div className="logs">
            <LogDisplay logs={logs} />
          </div>
          <p className="status-message">
            {isRunning ? "The system is running..." : "The system is stopped."}
          </p>
        </>
      )}
    </div>
  );
};

export default App;
