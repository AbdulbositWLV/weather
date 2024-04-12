const def_cityname = document.getElementById('default_location');
const realtime = document.getElementById('realtime');
const cityname = document.getElementById('cityname');
const humidity = document.getElementById('humidity');
const celsius = document.getElementById('celsius');
const feels = document.getElementById('feels');

const now = new Date();
const month = now.getMonth();
const day = now.getDate();
const hours = now.getHours();

let minutes = now.getMinutes();
if (minutes < 10) {
    minutes = '0' + minutes
}

const months = [
    'January', 'February', 'March', 'April',
    'May', 'June', 'July', 'August',
    'September', 'October', 'November', 'December'
]


const cachedData = localStorage.getItem('forecast')

if(cachedData){
    const data = JSON.parse(cachedData);
    main(data)
}else{
    const apiUrl = 'http://localhost:8088/weather';

    fetch(apiUrl)
    .then(response => response.json())
    .then(response => {
            console.log(response)
            localStorage.setItem('weatherData',JSON.stringify(response))
            main(response)
    })
    .catch(error=>console.log('Error', error))
}

main = function(data) {

    ind = data.length-1
    def_cityname.textContent = data[ind].city
    realtime.textContent = `${months[month]} ${day}, ${hours}:${minutes}`

    // firstIcon.src = `https://openweathermap.org/img/wn/${data[ind].weather[ind].icon}@2x.png`
    celsius.textContent = "Temperature " + Math.floor(data[ind].temp - 273)
    feels.textContent = "Feels like " + Math.floor(data[ind].feels_like - 273)

    humidity.textContent = "Humidity: " + data[ind].humidity + "%"

    return data[ind].name
    
}



// search_button.addEventListener("click", function(event) {
//     main(cityname.value)
// })
// cityname.addEventListener("keypress", function(event) {
//     if (event.keyCode == 13) {
//         main(cityname.value)

//     }
// })