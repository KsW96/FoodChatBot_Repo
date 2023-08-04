const message = document.getElementById("chat");
const submit = document.getElementById("submit");
const chatLog = document.getElementById("chatLog");
const resolved = document.getElementById("resolved");

function send(e) {
  if (e !== null) {
    e.preventDefault();
  }

  const obj = {
    chat: message.value,
  };

  addMessage("myMsg", message.value);
  scrollToBottom();

  fetch("http://192.168.0.113:8080/foodChatBot/chat", {
    method: "POST",
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    body: JSON.stringify(obj),
  })
    .then((resp) => resp.json())
    .then(handleResponse);
}

function handleResponse(data) {
  const food = data.food;
  message.value = "";

  addMessage("anotherMsg", food);
  
  if (food === "그것은 무엇?") {
    addOptions(["사람", "날씨", "장소", "재료", "행동"]);
  } else if (food === "그것은 어떤음식과 매칭?") {
    addOptions(["떡볶이", "갈비탕", "돈가스", "죽"]);
  }
  scrollToBottom();

  const resolve = data.resolve;
  resolved.innerHTML = resolve;
}

function addOptions(options) {
  const optionsElement = document.createElement("div");
  options.forEach((option) => {
    const button = document.createElement("button");
    button.textContent = option;
    button.onclick = () => handleOptionSelect(option);
    optionsElement.appendChild(button);
  });
  chatLog.appendChild(optionsElement);
}

function handleOptionSelect(option) {
  addMessage("myMsg", option);
  scrollToBottom();

  fetch("http://192.168.0.113:8080/foodChatBot/chat", {
    method: "POST",
    headers: { "Content-Type": "application/json;charset=utf-8" },
    body: JSON.stringify({ chat: option }),
  }).then((resp) => resp.json())
	.then((data) => {
 	 handleResponse(data);
	});
}

function addMessage(senderClass, message) {
  const msgDiv = document.createElement("div");
  msgDiv.classList.add(senderClass);
  msgDiv.innerHTML = '<span class="msg">' + message + "</span>";
  chatLog.appendChild(msgDiv);
}

window.addEventListener("load", function (e) {
  submit.addEventListener("click", send);
});

function scrollToBottom() {
  var chatLog = document.getElementById("chatLog");
  chatLog.scrollTop = chatLog.scrollHeight;
}