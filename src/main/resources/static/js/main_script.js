$(document).ready(function () {
    $(".tab_content").hide();
    $(".tab_content:first").show();

    $("ul.tabs li:first").addClass("active");

    $("ul.tabs li").click(function () {
        $("ul.tabs li").removeClass("active");
        $(this).addClass("active");
        $(".tab_content").hide();
        let activeTab = $(this).find("a").attr("href");
        $(activeTab).fadeIn();
        return false;
    });

    $.ajax({
        url: "/api/students",
        type: "GET",
        success: function (result) {
            function compare(a, b) {
                if (a.id < b.id) {
                    return -1;
                }
                if (a.id > b.id) {
                    return 1;
                }
                return 0;
            }

            result.sort(compare);

            let table =
                "<table class='table table-hover'><tr><th>Id</th><th>Name</th><th>Surname</th><th>Group</th></tr></thead><tbody>";
            for (const element of result) {
                let row =
                    "<tr>" +
                    "<td>" +
                    element.id +
                    "</td>" +
                    "<td>" +
                    element.firstName +
                    "</td>" +
                    "<td>" +
                    element.lastName +
                    "</td>";

                if (element.group == null) {
                    row += "<td> Free student </td>";
                } else {
                    row += "<td>" + element.group.name + "</td>";
                }

                row += "<tr/>";
                table += row;
            }
            table += "</table>";
            $("#students").html(table);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Exception: " + errorThrown);
        },
    });

    $.ajax({
        url: "/api/groups",
        type: "GET",
        success: function (result) {
            let table =
                "<table class='table table-hover'><thead><tr><th>Name</th><th>Students</th></tr></thead><tbody>";
            for (const element of result) {
                let row = "<tr><td>" + element.name + "</td><td>" + element.students + "</td></tr>";
                table += row;
            }
            table += "</tbody></table>";
            $("#groups").html(table);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Exception: " + errorThrown);
        },
    });

    $.ajax({
        url: "/api/courses",
        type: "GET",
        success: function (result) {
            let table =
                "<table class='table table-hover'><thead><tr><th>Name</th><th>Description</th></tr></thead><tbody>";
            for (const element of result) {
                let row = "<tr><td>" + element.name + "</td><td>" + element.description + "</td></tr>";
                table += row;
            }
            table += "</tbody></table>";
            $("#courses").html(table);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert("Exception: " + errorThrown);
        },
    });

    const add_student_form = document.getElementById("add-student-form");

    add_student_form.addEventListener("submit", (event) => {
        const formData = {
            firstName: $("#firstName").val(),
            lastName: $("#lastName").val(),
        };

        const data = JSON.stringify(formData);

        $.post({
            url: "/api/students/add",
            contentType: "application/json",
            data: data,
            success: function (response) {
                console.log("Success:", response);
            },
            error: function (error) {
                console.error("Error:", error);
            },
        });

        event.preventDefault();
    });

    const add_group_form = document.getElementById("add-group-form");

    add_group_form.addEventListener("submit", (event) => {
        const formData = {
            name: $("#groupName").val(),
        };

        const data = JSON.stringify(formData);

        $.post({
            url: "/api/groups/add",
            contentType: "application/json",
            data: data,
            success: function (response) {
                console.log("Success:", response);
            },
            error: function (error) {
                console.error("Error:", error);
            },
        });

        event.preventDefault();
    });

    const add_course_form = document.getElementById("add-course-form");

    add_course_form.addEventListener("submit", (event) => {
        const formData = {
            name: $("#courseName").val(),
            description: $("#courseDescription").val(),
        };

        const data = JSON.stringify(formData);

        $.post({
            url: "/api/courses/add",
            contentType: "application/json",
            data: data,
            success: function (response) {
                console.log("Success:", response);
            },
            error: function (error) {
                console.error("Error:", error);
            },
        });

        event.preventDefault();
    });
});
