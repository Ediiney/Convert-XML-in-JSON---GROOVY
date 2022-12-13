import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;

def Message processData(Message message) {
    
    def object =    '<note>'+
                    '<to>Tove</to>'+
                    '<from>Jani</from>'+
                    '<heading>Reminder</heading>'+
                    "<body>Don't forget me this weekend!</body>"+
                    '</note>'
    def jsonParse = new JsonSlurper()
    def final_json
    def json

    if (object[0] == "<") {
        
        def parsed = new XmlParser().parseText( object )
    
        // Deal with each node:
        def handle
        handle = { mode ->
          if( mode instanceof String ) {
              mode
          }
          else {
              [ (mode.name()): mode.collect( handle ) ]
          }
        }
        
        // Convert it to a Map containing a List of Maps
        def jsonObject = [ (parsed.name()): parsed.collect { mode ->
           [ (mode.name()): mode.collect( handle ) ]
        } ]
        
        // And dump it as Json
        json = new groovy.json.JsonBuilder( jsonObject )

        final_json = jsonParse.parseText(JsonOutput.toJson({}))   

        final_json.results = jsonParse.parseText(json.toString())

        message.setProperty("object", JsonOutput.prettyPrint(JsonOutput.toJson(final_json)))

    } else {

        final_json = jsonParse.parseText(JsonOutput.toJson({}))   

        json = [object]

        final_json.results = jsonParse.parseText(json.toString())

        message.setProperty("object", JsonOutput.prettyPrint(JsonOutput.toJson(final_json)))
 
    }
        

    return message;
}