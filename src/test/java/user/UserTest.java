package user;

import com.egs.account.model.Role;
import com.egs.account.model.User;
import com.egs.account.model.ajax.JsonUser;
import com.egs.account.utils.convertor.UserToJsonUserConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
public class UserTest {

    @Test
    public void testToJsonUser() {
        final User actualUser = constructUser();
        final JsonUser actualJsonUser = new JsonUser("hmkhitaryan", "Hayk", "Mkhitaryan", "hayk_84@mail.ru", "haykohayk1");
        final JsonUser expectedJsonUser = UserToJsonUserConverter.toJsonUser(actualUser);
        Assert.assertEquals(expectedJsonUser, actualJsonUser);
    }

    private User constructUser() {
        User user = new User();
        user.setId(1L);
        user.setUpdated(true);
        user.setUsername("hmkhitaryan");
        user.setFirstName("Hayk");
        user.setLastName("Mkhitaryan");
        user.setEmail("hayk_84@mail.ru");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_USER"));
        user.setRoles(roles);


        return user;
    }
}
