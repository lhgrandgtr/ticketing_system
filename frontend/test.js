fetch("http://localhost:8080/api/tickets/get-all-tickets")
  .then(response => response.json())
  .then(json => console.log(json))