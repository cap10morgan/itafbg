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
    const today = new Date();
    const todayId = "today-element";
    const targetElement = document.getElementById(todayId);
    const newElement = document.createElement("span");
    newElement.id = todayId;

    newElement.innerHTML = `Today is ${days[today.getDay()]}, ${months[today.getMonth()]} ${today.getDate()}, ${today.getFullYear()}`;
    targetElement.replaceWith(newElement);
}

