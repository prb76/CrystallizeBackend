$(function(){
var availableTags = [
    "ActionScript",
    "AppleScript",
    "Asp",
    "BASIC",
    "C",
    "C++",
    "Clojure",
    "COBOL",
    "ColdFusion",
    "Erlang",
    "Fortran",
    "Groovy",
    "Haskell",
    "Java",
    "JavaScript",
    "Lisp",
    "Perl",
    "PHP",
    "Python",
    "Ruby",
    "Scala",
    "Scheme"
];
function split( val ) {
    return val.split( / \s*/ );
}
function extractLast( term ) {
    return split( term ).pop();
}

$( "#query" )
    // don't navigate away from the field on tab when selecting an item
    .bind( "keydown", function( event ) {
        if ( event.keyCode === $.ui.keyCode.TAB &&
            $( this ).data( "autocomplete" ).menu.active ) {
            event.preventDefault();
        }
    })

    .autocomplete({
        minLength: 0,
        source: function( request, response ) {
            // delegate back to autocomplete, but extract the last term
            response( $.ui.autocomplete.filter(
                availableTags, extractLast( request.term ) ) );
            // response (["Hello1", "Hello2"]);
            // var japArray = getJapaneseWords(extractLast( request.term ));
            // response(japArray);
        },
        focus: function() {
            // prevent value inserted on focus
            return false;
        },
        select: function( event, ui ) {
            var terms = split( this.value );
            // remove the current input
            terms.pop();
            // add the selected item
            terms.push( ui.item.value );
            // add placeholder to get the space at the end
            terms.push( "" );
            this.value = terms.join( " " );
            return false;
        },
        open: function( event, ui ) {
            var input = $( event.target ),
                widget = input.autocomplete( "widget" ),
                style = $.extend( input.css( [
                    "font",
                    "border-left",
                    "padding-left"
                ] ), {
                    position: "absolute",
                    visibility: "hidden",
                    "padding-right": 0,
                    "border-right": 0,
                    "white-space": "pre"
                } ),
                div = $( "<div/>" ),
                pos = {
                    my: "left top",
                    collision: "none"
                },
                offset = -7; // magic number to align the first letter
                             // in the text field with the first letter
                             // of suggestions
                             // depends on how you style the autocomplete box

            widget.css( "width", "" );

            div
                .text( input.val().replace( /\S*$/, "" ) )
                .css( style )
                .insertAfter( input );
            offset = Math.min(
                Math.max( offset + div.width(), 0 ),
                input.width() - widget.width()
            );
            div.remove();

            pos.at = "left+" + offset + " bottom";
            input.autocomplete( "option", "position", pos );

            widget.position( $.extend( { of: input }, pos ) );
        }
    });

});

// function autoComp() {


// 	var fullInput = document.getElementById("dict").value;
// 	var inputList = fullInput.split(" ");
// 	var currentInput = inputList[inputList.length-1];
// 	var words = [];
// 	var url = window.location.origin;

// 	document.getElementById("dropdown").innerHTML = "";
// 	document.getElementById("dropdown").style.visibility = "hidden";

//     if (currentInput != "" && currentInput != " ") {
//     	showOptions(["Ford", "Volvo", "BMW"], event, currentInput);

//  		// url = url + "/autocomplete?input="+ currentInput;
//    //    	var xmlHttp = new XMLHttpRequest();
//    //    	evt = event;
//    //    	xmlHttp.onreadystatechange=function() {
// 	  //       if (xmlHttp.readyState==4 && xmlHttp.status==200) {
// 	  //           words = xmlHttp.responseText;
// 	  //           if (words != "") {
// 	  //             wordList = words.split(";");
// 	  //             showOptions(wordList, evt, currentInput);
// 	  //           }
// 	  //         }
//    //      }

//    //    xmlHttp.open("GET", url, true);
//    //    xmlHttp.send();
//   }
// }

// function applySelect() {
//   var drop = document.getElementById("dropdown");
//   document.getElementById("dict").value = drop.options[drop.selectedIndex].text;
// }

// function showOptions(wordList, evt, query) {
// 	alert("hello");
//   document.getElementById("dropdown").innerHTML = "";
//   for (i = 1; i < 3; i++) {
//     if (wordList[i] != undefined) {
//       document.getElementById("dropdown").size = i;
//       if (i <= parseInt(wordList[0]))
//         document.getElementById("dropdown").innerHTML = document.getElementById("dropdown").innerHTML + "<option style=\"color:#52188C;font-weight:bolder\" value=\"word" + i + "\">" + wordList[i] + "</option>";
//       else
//         document.getElementById("dropdown").innerHTML = document.getElementById("dropdown").innerHTML + "<option value=\"word" + i + "\">" + wordList[i] + "</option>";

//     }
//     else
//       break;
//     document.getElementById("dropdown").style.visibility = "visible";
//   }

//   var key = evt.keyCode || evt.charCode;

//     if( key > 48 && key < 90 ) {
//     if (wordList[1] != undefined) {
//       currentValue = query;
//       newValue = wordList[1];
//       start = currentValue.length;
//       end = newValue.length;
//       document.getElementById("keywords").value = newValue;
//       createSelection(document.getElementById("keywords"), start, end);
//     }
//   }
// }