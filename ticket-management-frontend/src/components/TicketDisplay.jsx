import { useEffect, useState } from "react";
import axios from "axios";
import PropTypes from "prop-types";

const TicketDisplay = ({ isRunning, setIsRunning }) => {
  const [ticketsLeft, setTicketsLeft] = useState(0);

  useEffect(() => {
    let intervalId;

    const fetchTickets = () => {
      axios
        .get("http://localhost:8080/api/tickets/count")
        .then((response) => {
          setTicketsLeft(response.data);
          if (response.data === 0 && isRunning) {
            setIsRunning(false); // Stop the system when no tickets are left
            console.log("All tickets are sold out. System stopped.");
          }
        })
        .catch((error) => {
          console.error("Error fetching ticket count:", error);
        });
    };

    if (isRunning) {
      fetchTickets();
      intervalId = setInterval(fetchTickets, 1000);
    }

    return () => clearInterval(intervalId);
  }, [isRunning, ticketsLeft, setIsRunning]);
};

TicketDisplay.propTypes = {
  isRunning: PropTypes.bool.isRequired,
  setIsRunning: PropTypes.func.isRequired,
};

export default TicketDisplay;
