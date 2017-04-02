var answerContainerSelector;

$(document).ready(function () {
    answerContainerSelector = $("#answerContainer");

    $('#refresh').on('click', function (){
        scroll("#questionFrame");
        answerContainerSelector.empty();
        $("#questionInput").val('');
    });
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
    scroll("#answerContainer");
}

function makeAnswerCard(answer, id) {
    var newAnswerCard = $('#answerCard').clone();
    newAnswerCard.css("display", "block");
    newAnswerCard.attr('id', 'answerCard' + id);
    newAnswerCard.find('#idAnswer').text(answer);
    return newAnswerCard;
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
