
const buttonStyles = {
  "Cancel": {
    class: "styled-add-cancel",
    color: "#EA0303",
    background: "#FCE0E0",
    border: "#EA0303"
  },
  "Back": {
    class: "styled-back",
    color: "#B91C1C",
    background: "#FEE2E2",
    border: "#B91C1C"
  },
  "Remove": {
    class: "styled-remove",
    color: "#D97706",
    background: "#FFF7E6",
    border: "#D97706"
  },
  "Delete": {
    class: "styled-delete",
    color: "#B91C1C",
    background: "#FEE2E2",
    border: "#B91C1C"
  },
  "No": {
    class: "styled-delete",
    color: "#B91C1C",
    background: "#FEE2E2",
    border: "#B91C1C"
  },
  "Not Required": {
    class: "styled-delete",
    color: "#B91C1C",
    background: "#FEE2E2",
    border: "#B91C1C"
  },
   "Close": {
    class: "styled-delete",
    color: "#B91C1C",
    background: "#FEE2E2",
    border: "#B91C1C"
  },
  "Ok": {
    class: "styled-ok",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
   "OK": {
    class: "styled-ok",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
  "Save": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
  "Yes": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
  "Send": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
   "Create": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
   "Submit": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
   "Save And Continue": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
   "Save as Draft": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
   "Draft": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
   "Accept": {
    class: "styled-save",
    color: "#03A096",
    background: "#E0F3F2",
    border: "#03A096"
  },
  "View": {
    class: "styled-view",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  },
  "Refresh": {
    class: "styled-refresh",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  },
  "Add": {
    class: "styled-add",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  },
  "Details": {
    class: "styled-add",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  },
  "Settlement": {
    class: "styled-add",
    color: "#03A096 ",
    background: "#E0F3F2",
    border: "#03A096 "
  },
  "Split Bill": {
    class: "styled-split",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  },
  "Update": {
    class: "styled-split",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  },
  "Select": {
    class: "styled-split",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  },
  "Add Item": {
    class: "styled-split",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  },
  "Continue": {
    class: "styled-split",
    color: "#2A83F2 ",
    background: "#EBF4FF",
    border: "#2A83F2 "
  }
};




function styleNamedButtons() {
  const buttons = document.querySelectorAll("button.btn.btn-primary");

  buttons.forEach(function (btn) {
    const text = btn.textContent.trim();
    const style = buttonStyles[text];

    if (style && !btn.classList.contains(style.class)) {
      btn.style.setProperty("color", style.color, "important");
      btn.style.setProperty("background-color", style.background, "important");
      btn.style.setProperty("border-color", style.border, "important");
      btn.style.setProperty("border-radius", "20px", "important");
      btn.classList.add(style.class);
    }
  });
}

const observer = new MutationObserver(styleNamedButtons);

observer.observe(document.body, {
  childList: true,
  subtree: true
});

document.addEventListener("DOMContentLoaded", styleNamedButtons);




function stylePlusButtons() {
  const buttons = document.querySelectorAll("button.btn.btn-info");

buttons.forEach(button => {
  const text = button.textContent.trim();

  if (text === "+" && !button.classList.contains("styled-plus-button")) {
    button.style.setProperty("border", "1px solid #2A83F2", "important");
    button.style.setProperty("background-color", "#EBF4FF", "important");
    button.style.setProperty("color", "#2A83F2", "important");
    button.style.setProperty("border-radius", "20px", "important");
    button.classList.add("styled-plus-button");
  }
});

}

document.addEventListener("DOMContentLoaded", function () {
  stylePlusButtons();

  const observer = new MutationObserver(stylePlusButtons);
  observer.observe(document.body, { childList: true, subtree: true });
});







