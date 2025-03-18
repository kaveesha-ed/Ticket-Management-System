import { useState } from "react";
import PropTypes from "prop-types";

const ConfigurationForm = ({ onSubmit }) => {
  const [totalTickets, setTotalTickets] = useState("");
  const [releaseRate, setReleaseRate] = useState("");
  const [retrievalRate, setRetrievalRate] = useState("");
  const [maxCapacity, setMaxCapacity] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    const config = {
      totalTickets: parseInt(totalTickets, 10),
      releaseRate: parseInt(releaseRate, 10),
      retrievalRate: parseInt(retrievalRate, 10),
      maxCapacity: parseInt(maxCapacity, 10),
    };
    onSubmit(config); // Pass configuration to the parent
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>System Configuration</h2>
      <div>
        <label>Total Tickets:</label>
        <input
          type="number"
          value={totalTickets}
          onChange={(e) => setTotalTickets(e.target.value)}
          required
        />
      </div>
      <div>
        <label>Ticket Release Rate (tickets per second):</label>
        <input
          type="number"
          value={releaseRate}
          onChange={(e) => setReleaseRate(e.target.value)}
          required
        />
      </div>
      <div>
        <label>Customer Retrieval Rate (tickets per second):</label>
        <input
          type="number"
          value={retrievalRate}
          onChange={(e) => setRetrievalRate(e.target.value)}
          required
        />
      </div>
      <div>
        <label>Maximum Ticket Capacity:</label>
        <input
          type="number"
          value={maxCapacity}
          onChange={(e) => setMaxCapacity(e.target.value)}
          required
        />
      </div>
      <button type="submit">Save Configuration</button>
    </form>
  );
};

ConfigurationForm.propTypes = {
  onSubmit: PropTypes.func.isRequired,
};

export default ConfigurationForm;
