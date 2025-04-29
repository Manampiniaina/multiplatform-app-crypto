
import { cryptoChart } from './chart.js';

document.addEventListener("DOMContentLoaded", () => {

    const socket = new WebSocket("ws://localhost:8080/changement-cours");

    socket.onmessage = function (event) {
      let data = JSON.parse(event.data);
      console.log("Mise à jour reçue :", data);
      let changes = data.changes ;
      let temps = data.temps ; 
      let series = Object.keys(changes).map(cryptoNom => {
        let data = changes[cryptoNom].map(entry => (entry.valeur));
      
        return {
          name: cryptoNom, // Le nom de la cryptomonnaie
          data: data       // Les données associées à cette cryptomonnaie
        };
      });

    //   // Mettez à jour le DOM
    //   document.getElementById("crypto-nom").innerText = data.nom;
    //   document.getElementById("crypto-cours").innerText = data.cours.toFixed(2) + " USD";
        cryptoChart("#areaChart", series, temps);
        console.log(data);
    };

    socket.onopen = function () {
      console.log("Connexion WebSocket ouverte");
    };

    socket.onclose = function () {
      console.log("Connexion WebSocket fermée");
    };

  });
// document.addEventListener("DOMContentLoaded", () => {

//     const seriesData = [
//       { name: "Bitcoin", data: [8107.85, 7056.0, 8000.9] },
//       { name: "Litecoin", data: [2300.5, 2720.8, 1990.9] },
//       { name: "Ethereum", data: [5107.85, 4056.0, 8000.9] }
//     ];
    
//     const times = [
//       1672579200000, // Exemples de timestamps
//       1672579260000,
//       1672579320000
//     ];
  
//     cryptoChart("#areaChart", seriesData, times);
//   });
  