
export function cryptoChart(elementId, series, times) {

    // Créer le graphique avec plusieurs séries
    // var series = "" ;
    // for (let index = 0; index < cryptomonnaies.length; index++) {
    //     series+= "{ name : \""+ cryptomonnaies[index]+"\", data"+valeurs[index]+",},"; 
    // }
    let timesInMilliseconds  = times.map(time => time * 1); 
    new ApexCharts(document.querySelector(elementId), {

      series: series,
      chart: {
        type: 'area',
        height: 350,
        zoom: {
          enabled: false
        }
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: 'smooth'
      },
      subtitle: {
        text: 'Cours des cryptomonnaies',
        align: 'left'
      },
       labels: timesInMilliseconds,  // Utilisation des timestamps en millisecondes pour l'axe X
        xaxis: {
        type: 'datetime',
        labels: {
          format: 'dd-MM-yyyy HH:mm:ss' // Format de l'heure
        }, 
        min: Math.min(...timesInMilliseconds),  // Définir la valeur minimale de l'axe X
        max: Math.max(...timesInMilliseconds),  // Définir la valeur maximale de l'axe X
        tickAmount: 5,  // Ajustez le nombre de ticks de l'axe X
      },
      yaxis: {
        opposite: false, // Aligner les valeurs sur le même axe
      },
      legend: {
        horizontalAlign: 'left'
      },
    }).render();
  }
  