import axios from "axios";

const ControlPanel = () => {
  const handleStart = () => {
    axios
      .post("http://localhost:8080/api/tickets/start") // Start operation
      .then((response) => {
        console.log(response.data); // Log the success message
      })
      .catch((error) => {
        console.error("Error starting the system:", error);
      });
  };

  const handleStop = () => {
    axios
      .post("http://localhost:8080/api/tickets/stop") // Stop operation
      .then((response) => {
        console.log(response.data); // Log the success message
      })
      .catch((error) => {
        console.error("Error stopping the system:", error);
      });
  };

  return (
    <div>
      <h2>Control Panel</h2>
      <button onClick={handleStart}>Start</button>
      <button onClick={handleStop}>Stop</button>
    </div>
  );
};

export default ControlPanel;
