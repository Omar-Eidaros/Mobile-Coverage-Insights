var response;
var BaseMap = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  maxZoom: 19,
  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
});

var mymap = L.map("mapid", {
  center: [30.1, 31.3],
  zoom: 10,
  layers: [BaseMap],
  maxBounds: L.latLngBounds(L.latLng(-90, -180), L.latLng(90, 180)),
  zoomDelta: 0.6,
  worldCopyJump: true,
  doubleClickZoom: false,
});


var obj = {}
var properties = { cell_id: "", signal_Level: "" }
function responseManipulation(response) {
  for (let i = 0; i < Object.size(response['measurement']); i++) {
    obj["properties"] = {
      IMSI: response['measurement'][i]['imsi'],
      IMEI: response['measurement'][i]['imei'],
      operator: response['measurement'][i]['operator'],
      country: response['measurement'][i]['country'],
      cell_id: response['measurement'][i]['cell_id'],
      status: response['measurement'][i]['signal_strength_level']
    }
    obj["type"] = "Feature";
    obj["geometry"] = { "type": "Point", "coordinates": [response['measurement'][i]['latitude'], response['measurement'][i]['longitude']] }
    var signal_level;
    if (Math.abs(parseInt(obj["properties"].status)) <= 60) {
      signal_level = "Excellent"
      x = L.circleMarker([obj["geometry"]["coordinates"][0], obj["geometry"]["coordinates"][1]],
        { radius: 23, color: "Green", weight: 3, fillOpacity: 0.5 }).addTo(mymap);
    }
    else if ( Math.abs(parseInt(obj["properties"].status)) <= 80 ) {
      signal_level = "Good"
      x = L.circleMarker([obj["geometry"]["coordinates"][0], obj["geometry"]["coordinates"][1]],
       { radius: 23, color: "yellow", weight: 3, fillOpacity: 0.5 }).addTo(mymap);
    } 
    else if(Math.abs(parseInt(obj["properties"].status)) <= 100){
      signal_level = "Poor"
      x = L.circleMarker([obj["geometry"]["coordinates"][0], obj["geometry"]["coordinates"][1]], { radius: 23, color: "Orange", weight: 3, fillOpacity: 0.5 }).addTo(mymap);
    }
    else {
      signal_level = "No Signal"
      x = L.circleMarker([obj["geometry"]["coordinates"][0], obj["geometry"]["coordinates"][1]], { radius: 23, color: "red", weight: 3, fillOpacity: 0.5 }).addTo(mymap);
    }

    x.bindPopup(`<div><span style="font-weight: bold;"> Cell ID : </span> ${obj['properties'].cell_id}<br><span style="font-weight: bold;"> IMSI : </span>${obj['properties'].IMSI}
 <br><span style="font-weight: bold;"> IMEI : </span>${obj['properties'].IMEI}
 <br><span style="font-weight: bold;"> Operator : </span>${obj['properties'].operator}
 <br><span style="font-weight: bold;"> Country : </span>${obj['properties'].country}
 <br><span style="font-weight: bold;"> latitude : </span>${obj.geometry.coordinates[0]}
 <br><span style="font-weight: bold;"> longitude : </span>${obj.geometry.coordinates[1]}
 <br><span style="font-weight: bold;"> Signal Strength : </span>${signal_level}</div>`);
  }
}
function DataShow() {
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "https://immense-journey-36861.herokuapp.com/measurment/DML/getAllMeasurements/we");
  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4) {
      if (xhr.status == 200) {
        response = JSON.parse(xhr.responseText);
        console.log(response);
        responseManipulation(response);
       
      }
    }
  }
  xhr.send("");
}
Object.size = function (obj) {
  var size = 0,
    key;
  for (key in obj) {
    if (obj.hasOwnProperty(key)) size++;
  }
  return size;
};

function fetchCount(){
  var time = document.getElementsByClassName('update-time');
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "https://immense-journey-36861.herokuapp.com/measurment/DML/countAll");
  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4) {
      if (xhr.status == 200) {
        response = JSON.parse(xhr.responseText);
        console.log(response)
        document.getElementById('countries').innerText = response['countriesNo'];
        document.getElementById('measurements').innerText = response['measurementsNo'];
        document.getElementById('cells').innerText = response['cellsNo'];
        document.getElementById('telecom-operators').innerText = response['operatorsNo'];

        for(let i=0;i<time.length;i++)
           time[i].innerHTML = response['date'];
      }
    }
  }
  xhr.send("");
}
DataShow();
document.getElementById("search-btn").addEventListener('click',function(){
  var xhr = new XMLHttpRequest();
  var long,lat;
  console.log(document.getElementById("address-search").value)
  xhr.open("GET", "http://api.positionstack.com/v1/forward?access_key=305db552c8ef2a0a37c5e8280165bc51&query="+ 
  document.getElementById("address-search").value+"&limit=1");
  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4) {
      if (xhr.status == 200) {
        response = JSON.parse(xhr.responseText);
        console.log(response);
        long = response["data"][0]["longitude"];
        lat  = response["data"][0]["latitude"]
        mymap.panTo(new L.LatLng(lat, long));
        DataShow();
      }
    }
  }
  xhr.send("");
  
  
});
fetchCount();