package com.egs.account.user;

import com.egs.account.model.Role;
import com.egs.account.model.User;
import com.egs.account.model.ajax.JsonUser;
import com.egs.account.utils.convertor.UserToJsonUserConverter;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
public class UserTest {

    @Mock
    private DomainUtils utilsService;

    @Mock
    private HttpServletRequest context;

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

    @Test
    public void testGetUserPrincipalName() {
        final String nullName = utilsService.getUserPrincipalName(context);

        Assert.assertEquals(null, nullName);
    }
}
