const message = document.getElementById("chat");
const submit = document.getElementById("submit");
const chatLog = document.getElementById("chatLog");
const resolved = document.getElementById("resolved");
let optionList = [];
let nope = 0;
const foodList = ["떡볶이", "갈비탕", "돈가스", "죽", "돈가스"];

function addOptionList(obj) {
  optionList.push(obj);
}

function fetchData(obj, method) {
  return fetch("http://localhost:8080/foodChatBot/chat", {
    method: method,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    body: JSON.stringify(obj),
  }).then((resp) => resp.json());
}

function send(e) {
  if (e !== null) {
    e.preventDefault();
  }

  // 여기서 이게 음식인지 확인해야함...
  if (foodList.includes(message.value)) {
    handleOptionSelect(message.value, "food");
  } else {
    addMessage("myMsg", message.value);
    fetchData({ chat: message.value }, "POST").then(handleResponse);
  }
  message.value = "";
}

function handleResponse(data) {
  // 추천의 답 -> chat : Y/N 날려줌
  if (data.answer !== undefined) {
    chatBotAnswer();
    if (data.answer === "") {
      addMessage("anotherMsg", "미안한데 못알아들었어. 다시 부탁해줄래?");
    }
    addOptionList((obj = { request: data.answer }));
    addMessage("anotherMsg", data.answer + " 어때?");
    addOptions(["그래", "아닌듯"], "request");
  }
  // 모르는 단어의 답 -> 단어의 정보들 날려줌
  if (data.request !== undefined) {
    addOptionList((obj = { request: data.request }));
    addMessage(
      "anotherMsg",
      "모르는 단어가 있다. '" + data.request + "'가 뭐임"
    );
    addOptions(
      ["사람", "날씨", "장소", "재료", "행동", "맛", "음식"],
      "category"
    );
  }
}

function addOptions(options, id) {
  const optionsElement = document.createElement("div");
  optionsElement.id = "optionsDiv";
  options.forEach((option) => {
    const button = document.createElement("button");
    button.textContent = option;
    button.onclick = () => handleOptionSelect(option, id);
    optionsElement.appendChild(button);
  });
  chatLog.appendChild(optionsElement);
  scrollToBottom();
}

function handleOptionSelect(option, id) {
  const obj = {
    [id]: option,
  };
  addOptionList(obj);
  console.log(optionList);
  addMessage("myMsg", option);
  if (id === "chat" && id === "request") {
    optionList.pop();
  }
  if (option === "아닌듯") {
    nope = nope + 1;
    addOptionList({ category: "거절" });
    addMessage("anotherMsg", "그럼 뭐먹고 싶은데?");
    fetchData(optionList, "PUT");
    console.log(optionList);
  } else if (id === "category" && option === "음식") {
    addMessage("anotherMsg", "오키 알았어.");
    fetchData(optionList, "PUT").then(handleResponse);
  } else if (id === "category" || option === "있어") {
    addMessage("anotherMsg", "그것은 어떤음식과 매칭되나?");
  } else if (id === "food") {
    addMessage("anotherMsg", "또 있음?");
    addOptions(["있어", "없어"], "chat");
  } else if (option === "없어") {
    fetchData(optionList, "PUT").then(handleResponse);
    optionList = [];
  }
  if (id !== "food") {
    document.getElementById("optionsDiv").remove();
  }
}

function chatBotAnswer() {
  let first = [
    "내가 추천하는 음식은 이거야!",
    "이걸 먹어보는 건 어때?",
    "그냥 이거 무조건 먹어라.",
  ];
  let second = [
    "흠.. 그러면 이건 어때?",
    "니 말을 알겠어. 이거 먹어",
    "그냥 이거 무조건 먹어라.",
  ];
  let third = [
    "너 되게 까다롭구나?",
    "제발 그냥 먹어라",
    "그냥 이거 무조건 먹어라.",
  ];
  var num = Math.floor(Math.random() * 4);

  if (nope === 0) {
    addMessage("anotherMsg", first[num]);
  } else if (nope === 1) {
    addMessage("anotherMsg", second[num]);
  } else {
    addMessage("anotherMsg", third[num]);
  }
}

function addMessage(senderClass, message) {
  const msgDiv = document.createElement("div");
  msgDiv.classList.add(senderClass);
  msgDiv.innerHTML = '<span class="msg">' + message + "</span>";
  chatLog.appendChild(msgDiv);
  scrollToBottom();
}

function scrollToBottom() {
  var chatLog = document.getElementById("chatLog");
  chatLog.scrollTop = chatLog.scrollHeight;
}

window.addEventListener("load", () => {
  submit.addEventListener("click", send);
});
