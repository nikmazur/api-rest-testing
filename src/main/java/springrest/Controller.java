package springrest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class Controller extends Data {

    //For ping requests. Randomly returns 404 error.
    @RequestMapping("/ping")
    public void ping() {
        if(PING_RAND)
            throw new ResourceNotFoundException();
    }

    //Returns current employee list
    @RequestMapping("/employees")
    public HashMap<Integer, HashMap<String, String>> comp() {
        return Data.getComp();
    }

    //Adds new employee. Returns the resulting contents of the map with the new data.
    @RequestMapping(value = "/employees/add", method = RequestMethod.POST)
    public HashMap<Integer, HashMap<String, String>> addEmpl(@RequestBody HashMap<String, String> input) {
        /*Check whether only numbers are passed in the numeric fields age & salary.
        This is done by trying to convert them to double (salary can be in double).
        In case of an exception error 405 is returned.*/
        try {
            double age = Double.parseDouble(input.get("age"));
            double salary = Double.parseDouble(input.get("salary"));
        } catch(NumberFormatException nfe) {
            throw new ResourceBadRequestException();
        }

        String name = input.get("name");
        String age = input.get("age");
        String salary = input.get("salary");

        //We also check that all fields are not empty. Data is then passed to the method.
        if(name != null && name.length() >0 && age != null && age.length() >0 &&
                salary.length() >0 && salary != null)
            Data.addEmpl(name, age, salary);
        else
            throw new ResourceBadRequestException();

        return Data.getComp();
    }

    /*Handles requests for deleting employees. Result is returned in the form of a string.
    Has two parameters - index & name, both optional. If neither provided, returns "Not found" message.*/
    @RequestMapping(value = "/employees/delete", method = RequestMethod.POST)
    public String delEmplIndex
            (@RequestParam(required = false, value = "ind") Integer index,
             @RequestParam(required = false, value = "name") String name) {

        if(getComp().get(index) != null) {
            delEmpl(index);
            return "Employee deleted";
        }
        else if(name != null && name.length() >0) {
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
