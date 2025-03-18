import { useEffect, useState } from "react";
import axios from "axios";

const LogDisplay = () => {
  const [logs, setLogs] = useState([]);

  // Fetch logs from the backend periodically
  useEffect(() => {
    const fetchLogs = () => {
      axios
        .get("http://localhost:8080/api/tickets/logs")
        .then((response) => {
          setLogs(response.data); // Update logs from backend
        })
        .catch((error) => {
          console.error("Error fetching logs:", error);
        });
    };

    fetchLogs(); // Initial fetch
    const intervalId = setInterval(fetchLogs, 1000); // Poll every second

    return () => clearInterval(intervalId); // Clean up interval on component unmount
  }, []);

  return (
    <div>
      <h2>Logs</h2>
      <ul>
        {logs.map((log, index) => (
          <li key={index}>{log}</li>
        ))}
      </ul>
    </div>
  );
};

export default LogDisplay;
