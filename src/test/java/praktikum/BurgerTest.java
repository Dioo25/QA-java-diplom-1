package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BurgerTest {

    private static final float BUN_PRICE = 100f;
    private static final float CHEESE_PRICE = 50f;
    private static final float KETCHUP_PRICE = 20f;

    private Burger burger;
    private Bun mockBun;
    private Ingredient cheeseIngredient;
    private Ingredient ketchupIngredient;

    @Before
    public void setUp() {
        burger = new Burger();
        mockBun = mock(Bun.class);
        cheeseIngredient = mock(Ingredient.class);
        ketchupIngredient = mock(Ingredient.class);

        when(mockBun.getName()).thenReturn("black bun");
        when(mockBun.getPrice()).thenReturn(BUN_PRICE);

        when(cheeseIngredient.getName()).thenReturn("cheese");
        when(cheeseIngredient.getPrice()).thenReturn(CHEESE_PRICE);
        when(cheeseIngredient.getType()).thenReturn(IngredientType.FILLING);

        when(ketchupIngredient.getName()).thenReturn("ketchup");
        when(ketchupIngredient.getPrice()).thenReturn(KETCHUP_PRICE);
        when(ketchupIngredient.getType()).thenReturn(IngredientType.SAUCE);
    }

    @Test
    public void setBuns_assignsBunToBurger() {
        burger.setBuns(mockBun);
        // single assertion per test: check bun name via getter if available, else via direct field as before
        assertEquals("black bun", burger.bun.getName());
    }

    @Test
    public void addIngredient_increasesIngredientsCount() {
        burger.addIngredient(cheeseIngredient);
        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void removeIngredient_clearsIngredientList() {
        burger.addIngredient(cheeseIngredient);
        burger.removeIngredient(0);
        assertTrue(burger.ingredients.isEmpty());
    }

    @Test
    public void moveIngredient_changesOrder() {
        burger.addIngredient(cheeseIngredient);
        burger.addIngredient(ketchupIngredient);
        burger.moveIngredient(0, 1);
        assertEquals("cheese", burger.ingredients.get(1).getName());
    }

    @Test
    public void getReceipt_returnsFullExpectedReceipt() {
        burger.setBuns(mockBun);
        burger.addIngredient(cheeseIngredient);
        burger.addIngredient(ketchupIngredient);

        String expected =
                "(==== black bun ====)\n" +
                        "= filling cheese =\n" +
                        "= sauce ketchup =\n" +
                        "(==== black bun ====)\n" +
                        "\nPrice: " + (BUN_PRICE * 2 + CHEESE_PRICE + KETCHUP_PRICE) + "\n";

        String actual = burger.getReceipt();
        assertEquals(expected, actual);
    }

    // Parameterized price tests separated into non-public class
    @RunWith(Parameterized.class)
    public static class BurgerPriceParameterizedTest {

        private Burger burger;
        private Bun bun;
        private Ingredient cheese;
        private Ingredient ketchup;

        private final Ingredient[] ingredients;
        private final float expectedPrice;

        public BurgerPriceParameterizedTest(Ingredient[] ingredients, float expectedPrice) {
            this.ingredients = ingredients;
            this.expectedPrice = expectedPrice;
        }

        @Before
        public void init() {
            burger = new Burger();

            bun = mock(Bun.class);
            when(bun.getPrice()).thenReturn(BUN_PRICE);

            cheese = mock(Ingredient.class);
            when(cheese.getPrice()).thenReturn(CHEESE_PRICE);

            ketchup = mock(Ingredient.class);
            when(ketchup.getPrice()).thenReturn(KETCHUP_PRICE);
        }

        @Parameterized.Parameters(name = "{index}: ingredients={0} expectedPrice={1}")
        public static Collection<Object[]> data() {
            // test cases: only bun, bun + cheese, bun + cheese + ketchup
            return Arrays.asList(new Object[][]{
                    {new Ingredient[]{}, BUN_PRICE * 2},
                    {new Ingredient[]{ /* cheese */}, BUN_PRICE * 2 + CHEESE_PRICE},
                    {new Ingredient[]{ /* cheese, ketchup */}, BUN_PRICE * 2 + CHEESE_PRICE + KETCHUP_PRICE}
            });
        }

        @Test
        public void parameterizedGetPrice_returnsExpected() {
            burger.setBuns(bun);

            // map placeholder to actual mocks
            if (ingredients.length == 1) {
                burger.addIngredient(cheese);
            } else if (ingredients.length == 2) {
                burger.addIngredient(cheese);
                burger.addIngredient(ketchup);
            }

            assertEquals(expectedPrice, burger.getPrice(), 0.0);
        }
    }
}