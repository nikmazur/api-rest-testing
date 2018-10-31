package springrest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class Controller extends Data {

    @RequestMapping("/ping")
    public void ping() {
        if(!isPingRand())
            throw new ResourceNotFoundException();
    }

    @RequestMapping("/employees")
    public HashMap<Integer, HashMap<String, String>> comp() {
        return Data.getComp();
    }

    @RequestMapping(value = "/employees/add", method = RequestMethod.POST)
    public HashMap<Integer, HashMap<String, String>> addEmpl(@RequestBody HashMap<String, String> input) {
        try {
            double age = Double.parseDouble(input.get("age"));
            double salary = Double.parseDouble(input.get("salary"));
        } catch(NumberFormatException nfe) {
            throw new ResourceBadRequestException();
        }

        String name = input.get("name");
        String age = input.get("age");
        String salary = input.get("salary");

        if(name != null && age != null && salary != null)
            Data.addEmpl(name, age, salary);
        else
            throw new ResourceBadRequestException();

        return Data.getComp();
    }

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

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {

}

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class ResourceBadRequestException extends RuntimeException {

}
