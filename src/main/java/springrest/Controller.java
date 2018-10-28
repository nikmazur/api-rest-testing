package springrest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class Controller extends Data {

    @RequestMapping("/ping")
    public HashMap<String, String> ping() {
        HashMap<String, String> res = new HashMap<>();

        if(isPingRand())
            pingOk(res);
        else
            throw new ResourceNotFoundException();

        return res;
    }

    @RequestMapping("/employees")
    public HashMap<Integer, HashMap<String, String>> comp() {
        return Data.getComp();
    }

    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public HashMap<Integer, HashMap<String, String>> addEmpl(@RequestParam("add") String command, @RequestBody HashMap<String, String> input) {
        String name = input.get("name");
        String age = input.get("age");
        String salary = input.get("salary");

        if(name != null && age != null && salary != null)
            Data.addEmpl(name, age, salary);
//        else
//            throw new ResourceBadRequestException();

        return Data.getComp();
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {

}

//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//class ResourceBadRequestException extends RuntimeException {
//
//}
