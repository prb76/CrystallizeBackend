var runningList = new Array();

document.addEventListener("keydown", KeyCheck);
function KeyCheck(event)
{
   var KeyID = event.keyCode;
   switch(KeyID)
   {
      case 8:
      var text = document.getElementById("query").value;
      if (text[text.length-1] != " ") {
        var allWords = text.split(" ");
        var wordToRemove = allWords[allWords.length-1];
        text = text.substring(0,text.lastIndexOf(wordToRemove));
        document.getElementById("query").value = text;
        runningList.pop();
      }
      break; 
      default:
      break;
   }
}

function mysubmit() {
    var responseArray = new Array();
    for (var i = 0; i < runningList.length; i++) {
        responseArray[i] = {FormID:0, Tags:null, WordID: runningList[i]};
    }
    alert(responseArray);
}

$(function(){
function split( val ) {
    return val.split( / \s*/ );
}
function extractLast( term ) {
    return split( term ).pop();
}

function getJapaneseWords(query) {
	var request = {};
	request.table = "Dictionary";
	
	var queryJSON = {};
	queryJSON.attribute = "English";
	queryJSON.operator = "CONTAINS";
	queryJSON.values = [query];

	request.query = queryJSON;
	console.log(request);
	
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/CrystallizeDynamoBackend/Query",
        data: request,
        dataType: 'json',
        contentType: 'application/json',
        crossDomain: true,
        success: function( res ) {
              var msg = '{ "results" : [' +
              '{ "ent_seq":1 , "jap":"Hello" },' +
              '{ "ent_seq":2 , "jap":"Hell" },' +
              '{ "ent_seq":3 , "jap":"Hi" } ]}';
              var jsonObject = JSON.parse(msg);
              var wordArray = new Array();
              var wordObjectArray = jsonObject["results"];
              for (i=0; i < wordObjectArray.length; i++) {
                  wordObject = wordObjectArray[i];
                  wordArray[i] = {label: wordObject["jap"], value: wordObject["ent_seq"]};
              }
              return wordArray;
        }
    	});
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
        minLength: 1,
        source: function( request, response ) {
            // delegate back to autocomplete, but extract the last term
            // response( $.ui.autocomplete.filter(
            //     availableTags, extractLast( request.term ) ) );
            wordArray = getJapaneseWords(extractLast( request.term ));
            response(wordArray);
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
            runningList.push(ui.item.value);
            // alert(runningList);
            terms.push( ui.item.label);
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