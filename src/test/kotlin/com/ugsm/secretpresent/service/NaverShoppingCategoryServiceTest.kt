import com.ugsm.secretpresent.dto.productcategory.BaseProductCategoryDto
import com.ugsm.secretpresent.dto.productcategory.ProductCategoryDto
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.repository.NaverShoppingCategoryRepository
import com.ugsm.secretpresent.repository.NaverShoppingCategoryRepositorySupport
import com.ugsm.secretpresent.service.NaverShoppingCategoryService
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class NaverShoppingCategoryServiceTest {

    @Mock
    private lateinit var repository: NaverShoppingCategoryRepository

    @Mock
    private lateinit var support: NaverShoppingCategoryRepositorySupport

    @InjectMocks
    private lateinit var service: NaverShoppingCategoryService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getAll should return list of NaverShoppingCategoryDto`() {
        // Given
        val childCategory = NaverShoppingCategory(
            id = 2,
            name = "Child",
            imageUrl = "child.jpg",
            parentCategory = null,
            isActive = true
        )
        val parentCategory = NaverShoppingCategory(id = 1, name = "Parent", imageUrl = "parent.jpg", isActive = true, childCategories = mutableListOf(childCategory))
        childCategory.parentCategory = parentCategory

        whenever(support.getAll()).thenReturn(listOf(parentCategory))

        // When
        val result = service.getAll()

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Parent", result[0].name)
        assertEquals("parent.jpg", result[0].imageUrl)
        assertNotNull(result[0].children)
        assertEquals(1, result[0].children?.size)
        assertEquals(2, result[0].children?.get(0)?.id)
        assertEquals("Child", result[0].children?.get(0)?.name)
        assertEquals("child.jpg", result[0].children?.get(0)?.imageUrl)
        assertEquals(1, result[0].children?.get(0)?.parentId)
    }

    @Test
    fun `getAllLeaves should return list of BaseProductCategoryDto`() {
        // Given
        val leaves = listOf(
            BaseProductCategoryDto(1, "Leaf 1"),
            BaseProductCategoryDto(2, "Leaf 2")
        )
        whenever(support.getAllLeaves()).thenReturn(leaves)

        // When
        val result = service.getAllLeaves()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Leaf 1", result[0].name)
        assertEquals(2, result[1].id)
        assertEquals("Leaf 2", result[1].name)
    }

    @Test
    fun `getAllCategories should return list of NaverShoppingCategoryDto with children`() {
        // Given
        val category1 = ProductCategoryDto().apply {
            this.id = 1
            this.name = "category1"
            this.imageUrl = "category1.jpg"
            this.childId = 2
            this.childName = "category2"
            this.childImageUrl = "category2.jpg"
        }
        val category2 = ProductCategoryDto().apply {
            this.id = 1
            this.name = "category1"
            this.imageUrl = "category1.jpg"
            this.childId = 3
            this.childName = "category3"
            this.childImageUrl = "category3.jpg"
        }
        val category3 = ProductCategoryDto().apply {
            this.id = 4
            this.name = "category4"
            this.imageUrl = "category4.jpg"
            this.childId = null
            this.childName = null
            this.childImageUrl = null
        }
        val categories = mutableListOf(category1, category2, category3)
        whenever(support.getAllCategories()).thenReturn(categories)

        // When
        val result = service.getAllCategories()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals("category1", result[0].name)
        assertEquals("category1.jpg", result[0].imageUrl)
        assertNotNull(result[0].children)
        assertEquals(2, result[0].children?.size)
        assertEquals(2, result[0].children?.get(0)?.id)
        assertEquals("category2", result[0].children?.get(0)?.name)
        assertEquals("category2.jpg", result[0].children?.get(0)?.imageUrl)
        assertEquals(3, result[0].children?.get(1)?.id)
        assertEquals("category3", result[0].children?.get(1)?.name)
        assertEquals("category3.jpg", result[0].children?.get(1)?.imageUrl)
    }
}