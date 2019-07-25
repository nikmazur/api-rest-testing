package springrest;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    public static ArrayList<Employee> addEmpl(@RequestBody Employee e) {

        //Check if name or title contains only numbers
        if(isNumeric(e.getName()) || isNumeric(e.getTitle()))
            throw new ResourceBadRequestException();

        //Checking input data: name or title not empty or only spaces (using trim()). Age & ID not 0.
        if(e.getName().trim().length() > 0 && e.getTitle().trim().length() > 0 && e.getAge() != 0 && e.getId() != 0) {
            return Data.addEmpl(e);
        }
        else
            throw new ResourceBadRequestException();
    }

    /* Handles requests for deleting employees. Result is returned as text.
     * Has two parameters - index & name, both optional. If neither provided, returns "Not found". */
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
