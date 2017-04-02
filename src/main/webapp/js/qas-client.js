var answerContainerSelector;
var loaderSelector;

$(document).ready(function () {
    answerContainerSelector = $("#answerContainer");
    loaderSelector = $("#loader");

    $('#refresh').on('click', function (){
        scroll("#questionFrame");
        answerContainerSelector.empty();
        $("#questionInput").val('');
    });
});

function ask() {
    answerContainerSelector.empty();
    loaderSelector.show();

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
        success: answer,
        error: exception
    });
}

function answer(answers) {
    loaderSelector.hide();

    console.log("QAS response: " + JSON.stringify(answers));
    if (answers.length == 0) answers[0] = "Ответов не найдено";
    for (var i = 0; i < answers.length; i++) {
        var answer = answers[i];
        var answerCard = makeAnswerCard(answer, i);
        answerContainerSelector.append(answerCard);
    }
    scroll("#answerContainer");
}

function makeAnswerCard(answer, id) {
    var newAnswerCard = $('#answerCard').clone();
    newAnswerCard.css("display", "block");
    newAnswerCard.attr('id', 'answerCard' + id);
    newAnswerCard.find('#idAnswer').text(answer);
    return newAnswerCard;
}

function exception() {
    loaderSelector.hide();
    console.log("An error occurred while accessing the QAS");
    var errorMsg = "Возниколо исключение. Проверьте корректность вводимых данных";
    var answerCard = makeAnswerCard(errorMsg, 0);
    answerContainerSelector.append(answerCard);
}


//common
$(window).scroll(function() {
    $(".slideanim").each(function(){
        var pos = $(this).offset().top;
        var winTop = $(window).scrollTop();
        if (pos < winTop + 2000) {
            $(this).addClass("slide");
        }
    });
});

function scroll(hash) {
    $('html, body').animate({
        scrollTop: $(hash).offset().top
    }, 3000, function () {
        window.location.hash = hash;
    });
}
