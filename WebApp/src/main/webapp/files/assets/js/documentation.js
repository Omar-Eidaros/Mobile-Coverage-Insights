
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

fetchCount();