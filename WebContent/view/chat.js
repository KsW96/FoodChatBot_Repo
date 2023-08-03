var url = "http://192.168.0.113:8080/foodChatBot/chat";
let message = document.getElementById("message");
let submit = document.getElementById("submit");
let chatLog = document.getElementById("chatLog");
let resolved = document.getElementById("resolved");

function send(e) {
  e.preventDefault();

  let obj = {
    chat: chat.value,
  };

  fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    body: JSON.stringify(obj),
  });

  let myMsgDiv =
    '<div class="myMsg"><span class="msg">' + chat.value + "</span></div>";
  chatLog.insertAdjacentHTML("beforeend", myMsgDiv);

  bot(e);
}

function bot(e) {
  e.preventDefault();

  fetch(url)
    .then((resp) => resp.json())
    .then((data) => {
      let food = data.food;
	  let img_url = data.IMG_URL;

      let botMsgDiv =
        '<div class="anotherMsg"><span class="msg">' + food + img_url+"</span></div>";
      chatLog.insertAdjacentHTML("beforeend", botMsgDiv);

      let resolve = data.resolve;
      resolved.innerHTML = resolve;
    });
}

window.addEventListener("load", function (e) {
  submit.addEventListener("click", send);
});
