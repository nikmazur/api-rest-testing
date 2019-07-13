package springrest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class Controller extends Data {

    //For ping requests. Randomly returns 404 error.
    @RequestMapping("/ping")
    public void ping() {
        if(PING_RAND)
            throw new ResourceNotFoundException();
    }

    //Returns current employees list
    @RequestMapping("/employees")
    public ArrayList<Employee> comp() {
        return Data.getComp();
    }

    //Adds new employee. Returns the resulting contents of the map with the new data.
    @RequestMapping(value = "/employees/add", method = RequestMethod.POST)
    public ArrayList<Employee> addEmpl(@RequestBody HashMap<String, String> input) {
        /* Check whether only numbers are passed in the numeric fields age & salary.
         * This is done by trying to convert them to double. In case of an exception error 405 is returned. */
        double age;
        int ID;
        try {
            age = Double.parseDouble(input.get("age"));
            //We also check and remove any commas from salary using the replace() method
            ID = Integer.parseInt(input.get("id"));
        } catch(NumberFormatException nfe) {
            throw new ResourceBadRequestException();
        }

        String name = input.get("name").replace(",", "");
        String title = input.get("title");

        //Checking input data: name not null, empty, or only spaces (using trim()). Age & salary not 0.
        //Data is then passed to the method.
        if(name.trim().length() > 0 && title.trim().length() > 0 && age != 0 && ID != 0)
            Data.addEmpl(ID, name, title, age);
        else
            throw new ResourceBadRequestException();

        return Data.getComp();
    }

    /* Handles requests for deleting employees. Result is returned in the form of a string.
     * Has two parameters - index & name, both optional. If neither provided, returns "Not found" message. */
    @RequestMapping(value = "/employees/delete", method = RequestMethod.POST)
    public String delEmplIndex
            (@RequestParam(required = false, value = "ind") Integer index,
             @RequestParam(required = false, value = "name") String name) {

        if(index != null) {
            if(getComp().get(index) != null) {
                delEmpl(index);
                return "Employee deleted";
            }
            else
                return "Employee not found";
        }
        else if(name != null && name.length() > 0) {
            if(delEmplName(name))
                return "Employee deleted";
            else
                return "Employee not found";
        }
        else
            return "Employee not found";
    }
}

//Error 404
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {

}

//Error 400
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class ResourceBadRequestException extends RuntimeException {

}
