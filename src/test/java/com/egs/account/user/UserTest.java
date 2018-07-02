package com.egs.account.user;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserTest {

//    @Mock
//    private DomainUtils utilsService;
//
//    @Mock
//    private HttpServletRequest context;
//
//    @Test
//    public void testToJsonUser() {
//        final User actualUser = constructUser();
//        final JsonUser actualJsonUser = new JsonUser("hmkhitaryan", "Hayk", "Mkhitaryan", "hayk_84@mail.ru", "haykohayk1");
//        final JsonUser expectedJsonUser = UserToJsonUserConverter.toJsonUser(actualUser, true);
//        Assert.assertEquals(expectedJsonUser, actualJsonUser);
//    }
//
//    private User constructUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setUpdated(true);
//        user.setUsername("hmkhitaryan");
//        user.setFirstName("Hayk");
//        user.setLastName("Mkhitaryan");
//        user.setEmail("hayk_84@mail.ru");
//        Set<Role> roles = new HashSet<>();
//        roles.add(new Role("ROLE_USER"));
//        user.setRoles(roles);
//
//
//        return user;
//    }
//
//    @Test
//    public void testGetUserPrincipalName() {
//        final String nullName = utilsService.getUserPrincipalName(context);
//
//        Assert.assertEquals(null, nullName);
//    }
}
