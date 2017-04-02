var answerContainerSelector;

$(document).ready(function () {
    answerContainerSelector = $("#answerContainer");
});

function ask() {
    answerContainerSelector.empty();

    var question = $('#questionInput').val();
    console.log("Ask question: " + question);

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/qas/ask",
        data: {
            question: question
        },
        dataType: "json",
        timeout: 100000,
        success: answer
    });
}

function answer(answers) {
    console.log("QAS response: " + JSON.stringify(answers));
    for (var i = 0; i < answers.length; i++) {
        var ans = answers[i];
        var answerCard = makeAnswerCard(ans, i);
        answerContainerSelector.append(answerCard);
    }
}

function makeAnswerCard(answer, id) {
    var newAnswerCard = $('#answerCard').clone();
    newAnswerCard.css("display", "block");
    newAnswerCard.attr('id', 'answerCard' + id);
    newAnswerCard.find('#idAnswer').text(answer);
    return newAnswerCard;
}


