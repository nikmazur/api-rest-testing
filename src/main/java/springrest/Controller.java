package springrest;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@RestController
public class Controller extends Data {

    //For ping requests. Randomly returns 404 error.
    @RequestMapping("/ping")
    public void ping() {
        if(RandomUtils.nextBoolean())
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
        int age;
        int ID;
        try {
            age = Integer.parseInt(input.get("age"));
            //We also check and remove any commas from salary using the replace() method
            ID = Integer.parseInt(input.get("id"));
        } catch(NumberFormatException nfe) {
            throw new ResourceBadRequestException();
        }

        String name = input.get("name").replace(",", "");
        String title = input.get("title");

        //Check if name or title contains only numbers
        if(isNumeric(name) || isNumeric(title))
            throw new ResourceBadRequestException();

        //Checking input data: name not empty or only spaces (using trim()). Age & salary not 0.
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

        if(index != null && getComp().get(index) != null) {
                delEmpl(index);
                return "Employee deleted";
        }
        else if(name != null && delEmplName(name)) {
                return "Employee deleted";
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
