const days = [
    "Sunday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday"
];

const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
];

function setTodayElement() {
    var today = new Date();
    var targetElement = document.getElementById("today-element");
    var newElement = document.createElement("span");

    newElement.innerHTML = `Today is ${days[today.getDay()]}, ${months[today.getMonth()]} ${today.getDate()}, ${today.getFullYear()}`;
    targetElement.replaceWith(newElement);
}

