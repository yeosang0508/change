$(document).ready(function () {
    $(".message").click(function () {
        $(".message").addClass("hidden");
        $(".message2").addClass("hidden");
    });
    $(".message2").click(function () {
        $(".message").addClass("hidden");
        $(".message2").addClass("hidden");
    });
});