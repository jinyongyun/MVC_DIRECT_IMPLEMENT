# MVC_DIRECT_IMPLEMENT
MVC framework 기능 직접 구현하여 흐름 알아보기!
https://www.notion.so/MVC-34d2ebfbbe464d0fa0ab6813c27a6887?pvs=4


	
<body><article id="34d2ebfb-be46-4d0f-a0ab-6813c27a6887" class="page sans"><header>MVC 프레임워크 만들기</h1><p class="page-description"></p><table class="properties"><tbody></tbody></table></header><div class="page-body"><h1 id="7fff3590-632b-4544-b7cd-0416a22863bf" class="">Reflection API 개념 소개 및 실습</h1><h2 id="90fc4d2f-902a-4416-ad57-a3ecd611357d" class="">Reflection</h2><p id="29407748-66b7-4a51-86b3-f96fc503bb9c" class="">힙 영역에 로드돼 있는 클래스 타입의 객체를 통해 필드/메소드/생성자를 접근 제어자와 상관 없이 사용할 수 있도록 지원하는 API</p><p id="048bb4f1-c6b3-4291-b6e0-b49599f54011" class="">힙 영역에 로드되어 있는 클래스 타입의 객체를 가져오는 방법으로는 세 가지가 있는데 </p><ul id="a26656b3-415b-43d3-8278-2c0bf79f939f" class="bulleted-list"><li style="list-style-type:disc">클래스.클래스</li></ul><ul id="b13fc1b3-7f11-46a0-8b0c-1fb93ace224b" class="bulleted-list"><li style="list-style-type:disc">인스턴스.get클래스</li></ul><ul id="7a27bd22-fe8c-4b10-a774-e7d099f54522" class="bulleted-list"><li style="list-style-type:disc">클래스.forName</li></ul><p id="aac618f3-0acf-4ae7-94e8-9027b3217373" class="">이 세 가지 방법이 있다.</p><p id="5f9c6063-15eb-4cb5-969e-5c1b151b0bfc" class="">참고로 JVM의 클래스 로더는 클래스 파일에 대한 로딩이 끝나면 클래스 타입의 객체를 생성해서 메모리 힙 영역에 저장한다.</p><p id="505d3aa7-760d-4a04-aff3-c407a5658361" class="">또한 리플랙션은 컴파일 시점이 아닌 런타임 시점에 동적으로 특정 클래스의 정보를 추출해낼 수 있는 프로그래밍 기법이다.</p><p id="894d3de6-5a33-4014-9e09-0502e3ed224f" class="">주로 프레임워크 또는 라이브러리 개발 시에 리플랙션을 사용한다.</p><p id="60a6405d-28a9-4b8a-8c8d-07cb3f9b0209" class="">
</p><h2 id="c81b2aeb-528e-4c2d-b424-0bf0c15d790e" class="">Reflection을 사용하는 프레임워크/라이브러리 소개</h2><ul id="a8e1e7c5-9965-4100-8778-0e3a9c69485d" class="bulleted-list"><li style="list-style-type:disc">Spring 프레임워크 (ex. DI)</li></ul><ul id="4d1d3ce5-8349-411b-8615-7c196b4c25ad" class="bulleted-list"><li style="list-style-type:disc">Test 프레임워크 (ex. JUnit)</li></ul><ul id="7a8967e1-abd1-48c0-aabd-3e95503fbe75" class="bulleted-list"><li style="list-style-type:disc">JSON Serialization/Deserialization 라이브러리 (ex. Jackson)</li></ul><ul id="c24675d8-aef8-4eaf-b05c-c0e99d92599b" class="bulleted-list"><li style="list-style-type:disc">기타등등</li></ul><p id="bb660b57-70c9-4cc5-abd8-b599c6fff1c6" class="">
</p><h2 id="fb904658-9850-4c99-b17a-c711234f9864" class="">실습</h2><p id="a6e1087c-28e4-45e3-8220-a676d920e283" class="">@Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다.</p><p id="7d1665f6-1519-495d-a46d-7189dceb4c25" class="">dependency는 다음과 같이 구성</p><pre id="a98a1322-d559-4613-bc40-438b0543ea56" class="code"><code>plugins {
    id &#x27;java&#x27;
}

group &#x27;org.example&#x27;
version &#x27;1.0-SNAPSHOT&#x27;

repositories {
    mavenCentral()
}

dependencies {
    implementation &#x27;javax.servlet:javax.servlet-api:4.0.1&#x27;

    implementation &#x27;org.reflections:reflections:0.10.2&#x27;

    testImplementation &#x27;org.junit.jupiter:junit-jupiter-api:5.8.1&#x27;
    testRuntimeOnly &#x27;org.junit.jupiter:junit-jupiter-engine:5.8.1&#x27;

    testImplementation &#x27;org.assertj:assertj-core:3.23.1&#x27;
}

test {
    useJUnitPlatform()
}</code></pre><p id="af60367f-a8eb-4893-8a8d-2f32f958a605" class="">먼저 mvc_practice 만들고</p><p id="d3443000-0b5a-40a4-bdd8-c674e239c81c" class="">java&gt;main&gt;org.example&gt;annotation&gt;Controller 애너테이션 만들기</p><pre id="6758246e-11fa-4575-b9e3-9ecb1dbf2746" class="code"><code>package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // 해당 annotation을 어디가 붙일 건지 -&gt; 클래스나 인터페이스나 enum 선언 등 타입에 붙이겠다.
@Retention(RetentionPolicy.RUNTIME) //해당 annotation의 유지기간은 runtime 기간까지
public @interface Controller {

}</code></pre><p id="5605d12d-a32b-44ae-8716-6b2c3e4bbc7a" class="">해당 annotation이 붙어져있는 클래스, 컨트롤러를 만들어두도록 하겠다.</p><p id="4280e72b-e3c6-4d6a-ab3c-a7139228fcac" class="">패키지 밑에 controller 패키지를 만들고, HomeController 클래스를 만든다</p><pre id="2fa4deb5-c24b-457d-88a8-ccbf66bc96ad" class="code"><code>package org.example.controller;

import org.example.annotation.Controller;
import org.example.annotation.RequestMapping;


@Controller
public class HomeController {
    @RequestMapping(&quot;/&quot;)
    public String home(HttpServletRequest request, HttpServletResponse response){
        return &quot;home&quot;;
    }
}</code></pre><p id="5ae69d95-9e3f-4903-b405-27de68726a87" class="">RequestMapping 애너테이션을 직접 만들어 준다.</p><p id="5ff9b59d-6225-4614-96fb-93c477eb0cc6" class="">아까 만들었던 annotation 패키지 밑에 RequestMapping 애너테이션을 만들어준다.</p><p id="654f1e63-a50b-4486-bb68-6c1248dfef07" class="">해당 애너테이션은 메소드와 클래스에 붙을 수 있다고 타깃을 설정해주고</p><p id="837dd28b-b2ba-4e05-9782-aa64216cf0e4" class="">RequestMapping 할 때 Get 요청이냐 Post 요청이냐에 따라 Method를 지정해 줄 수 있으니 </p><p id="d525560a-c826-4d90-9ed7-c66620c8ad27" class="">get요청인지 post요청인지에 따른 RequestMethod 설정해주도록 한다.</p><pre id="65ed2416-af7f-4824-af5c-5905b5fb8631" class="code"><code>package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD}) //annotation이 클래스와 메서드에도 붙을 수 있다.
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default &quot;&quot;; // String value 메소드는 어떤 값도 입력하지 않으면 빈 문자열이 디폴트

    RequestMethod[] method() default {};
}</code></pre><p id="ca721852-b366-4477-b4d7-b7b05cfc0c23" class="">RequestMapping 애너테이션에 필요한 RequestMethod는 간단하게 enum으로 만들어준다.</p><pre id="414d38be-8e04-4162-aa9c-457c355b8fd9" class="code"><code>package org.example.annotation;

public enum RequestMethod {
    GET, POST, PUT, DELETE
}</code></pre><p id="72bf8c7b-b249-42c7-8123-3cedd7d145ac" class="">그럼 이제 실습을 하기 위한 운용 코드들은 준비가 됐다!</p><p id="bb789d17-17df-45c8-b5e4-d1fd9beb8431" class="">
</p><p id="eca5c001-c749-4667-85e3-b9d07906f7d4" class="">이제 test코드를 한 번 만들어보자!!</p><p id="cc637e7a-46c0-4868-90cb-6e315d207743" class="">test&gt;java&gt;org.example&gt;ReflectionTest를 만들어주도록 하자</p><pre id="68843aca-8d0f-4a0b-acc6-77ea57ce8cb9" class="code"><code>import java.util.HashSet;
import java.util.Set;

/**
 * @Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다.
 *
 */
public class ReflectionTest {

    @Test
    void controllerScan(){
         Reflections reflections = new Reflections(&quot;org.example&quot;); //패키지는 어디서 찾을 것인가. 즉 org.example 패키지 밑에 있는 모든 클래스 대상으로 reflection 사용 

        Set&lt;Class&lt;?&gt;&gt; beans = new HashSet&lt;&gt;();
        beans.addAll(reflections.getTypesAnnotatedWith(Controller.class));
    }
}</code></pre><p id="3326cb39-12d9-4e75-9a24-e003147658cb" class="">설명을 하자면, org.example 밑에 있는 클래스들 안에서 Controller가 붙어있는 클래스들을 찾는다-라는 의미이다. 즉 해당 패키지(org.example) 밑에 있는 클래스들 안에 controller 애너테이션이 붙어 있는 모든 것들을 hashSet안에 다 담는 것이다.</p><p id="fd04bc38-a69d-44b8-97df-54a67869c251" class="">beans 안에 전부 담겼으니 이를 logger로 찍어봐야해서, dependency에 로그도 추가하고 </p><p id="5678b9c0-3e24-4f39-a674-c76c84ec0473" class="">코드도 수정했다.</p><pre id="95c4b9af-237f-4301-aa8e-c4ee93baa822" class="code"><code>package org.example;

import org.example.annotation.Controller;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다.
 *
 */
public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void controllerScan(){
         Reflections reflections = new Reflections(&quot;org.example&quot;); //패키지는 어디서 찾을 것인가. 즉 org.example 패키지 밑에 있는 모든 클래스 대상으로 reflection 사용

        Set&lt;Class&lt;?&gt;&gt; beans = new HashSet&lt;&gt;();
        beans.addAll(reflections.getTypesAnnotatedWith(Controller.class));

        logger.debug(&quot;beans: [{}]&quot;, beans);
    }
}</code></pre><p id="a5bd0ac5-9b34-464c-8d46-1b3c2c979400" class="">테스트를 실행시켜보면…</p></a></figure><p id="4eea63d6-acb0-4bd9-8cae-2a74e19218d5" class="">홈컨트롤러가 유일하게 Controller 애너테이션이 붙어있는 클래스라고 나온 것을 볼 수 있다.</p><p id="9e21c3fc-45ea-47ac-a1ce-601ceb9f164e" class="">정말 그런지, 다른 클래스를 추가해서 알아보겠다.</p><pre id="2faff46a-f916-4e29-ae5d-d8ddfd7d0af3" class="code"><code>package org.example.controller;

import org.example.annotation.Controller;
import org.example.annotation.RequestMapping;
import org.example.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class HealthCheckController {
    @RequestMapping(value = &quot;/health&quot;, method = RequestMethod.GET)
    public String home(HttpServletRequest request, HttpServletResponse response){
        return &quot;ok&quot;;
    }
}</code></pre><p id="65e1dcf5-97ce-4369-ba32-7df0989ca01a" class="">간단한 HealthCheckController를 추가하고 테스트를 실행시켰는데</p></a></figure><p id="d340a612-712f-4e94-997d-b68f67550774" class="">HealthCheckController도 들어온 것을 볼 수 있다.</p><p id="a6081e50-bcfe-4abe-8681-16aa3929b2c7" class="">즉 Controller 애너테이션이 붙어있는 모든 클래스들을 찾았고</p><p id="83560ad2-86f1-4c67-a66d-6f79c4610da0" class="">이를 출력하는 테스트 코드를 작성해보았다.</p><p id="59dda17b-a1f3-4756-97c5-79ce65a6aabd" class="">
</p><p id="c8c81d16-e149-4df8-871f-520b753413c2" class="">이번에는 더 나아가서 Service라는 애너테이션이 붙어 있는 클래스들을 전부 찾아보도록 하자</p><p id="354ccc36-c1e1-4c15-825a-2a76581f6f90" class="">controller 패키지에 HomeService 클래스를 만들고</p><p id="668d698d-fa12-412f-b157-a51a8c18ee7a" class="">annotation 패키지에 Service 애너테이션을 만든다.</p><pre id="8aaa368b-c6e8-43da-a5b5-5461bcf9773b" class="code"><code>package org.example.controller;

import org.example.annotation.Service;

@Service
public class HomeService {


}</code></pre><pre id="9bf23aec-eb5b-47f5-8459-49beca1bcd93" class="code"><code>package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}</code></pre><p id="e3ac538a-db35-406a-87b3-613e6823118d" class="">테스트를 다음과 같이 수정한 뒤 테스트를 실행시키면</p><p id="9df0e44a-2f6d-400a-959b-edb4ad3d3ed2" class="">
</p></a></figure><p id="641133ef-524b-4354-ac1f-6fcffa926797" class="">이렇게 HomeService 클래스도 hash에 들어가 있는 것을 볼 수 있고</p><p id="a99b9f63-76aa-4825-a94f-e8a309f2ea46" class="">약간의 리팩토링을 거치면 다음과 같은 코드가 완성이 된다.</p><pre id="02f86c3c-9746-46e3-9929-283b1bdcab7f" class="code"><code>package org.example;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다.
 *
 */
public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void controllerScan(){
        Set&lt;Class&lt;?&gt;&gt; beans = getTypesAnnotatedWith(List.of(Controller.class, Service.class));
        logger.debug(&quot;beans: [{}]&quot;, beans);
    }

    private Set&lt;Class&lt;?&gt;&gt; getTypesAnnotatedWith(List&lt;Class&lt;? extends Annotation&gt;&gt; annotations) {
        Reflections reflections = new Reflections(&quot;org.example&quot;); //패키지는 어디서 찾을 것인가. 즉 org.example 패키지 밑에 있는 모든 클래스 대상으로 reflection 사용

        Set&lt;Class&lt;?&gt;&gt; beans = new HashSet&lt;&gt;();
        annotations.forEach(annotation -&gt; beans.addAll(reflections.getTypesAnnotatedWith(annotation)));
        return beans;
    }
}</code></pre><p id="4110db95-84c3-4fe6-851a-b7d90e83a4b8" class="">그 다음으로 만들어 볼 것은 class에 선언된 모든 필드를 보여주는 테스트이다. </p><p id="80ebab07-1e07-49b3-b40b-ec4f175f5f38" class="">준비물인 User 클래스는 org.example&gt;model 패키지 밑에다 만들어주고</p><pre id="0c336441-c38d-4e5b-b9d7-bedaf88c9da9" class="code"><code>package org.example.model;

import java.util.Objects;

public class User {
    private String userId;
    private String name;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public boolean equalsUser(User user){
        return this.equals(user);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &amp;&amp; Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name);
    }
}</code></pre><p id="67b6c5ed-3507-439a-85bb-d9954f35d09d" class="">ReflectionTest에 다음과 같은 테스트 메서드를 추가해준다.</p><pre id="5e3d58e2-6058-4af9-935e-53e079f67b58" class="code"><code>@Test
    void showClass() {
        Class&lt;User&gt; clazz = User.class;
        logger.debug(clazz.getName());
        logger.debug(&quot;User all declared fields: [{}]&quot;, Arrays.stream(clazz.getDeclaredField()).collect(Collectors.toList()));
    }</code></pre><p id="f8ee5dff-6fd5-4849-8b0e-384063f30f40" class="">테스트코드를 돌려보면…</p></a></figure><p id="259d7c58-b9d1-4890-bf2f-c70b2cd09546" class="">다음과 같이 생성자와 메서드도 볼 수 있다.</p><p id="62cf47d0-a294-40f6-8da3-c27ec25d5af6" class="">
</p><p id="65f0f81b-5513-4604-8765-39dd15654b7b" class="">실습 마지막으로 제일 앞부분에서 이야기 했던 힙 영역에 로드되어 있는 클래스 타입의 객체를 가져오는 방식을 보여주려 한다.</p></a></figure><p id="f4579391-8876-4774-93b2-cf52db69396a" class="">보다 확실히 하기 위해 assertThat을 추가해서 객체끼리 같은 지 비교해 보아도 전부 같다는 것을 알 수 있다.</p></a></figure><p id="19904d8b-8232-4043-8fd3-67ed4e04f3f2" class="">
</p><h2 id="8b3426f8-cc8a-4887-8109-1e8f88bb92ee" class="">프런트 컨트롤러 패턴 개념 소개</h2><p id="0f40720c-d5cb-43a0-a14c-9b93fa2d9742" class="">프런트 컨트롤러 패턴</p><ul id="8867f9a6-5687-4578-bf11-ed870334ea19" class="bulleted-list"><li style="list-style-type:disc">모든 요청을 단일 handler(처리기)에서 처리하도록 하는 패턴</li></ul><ul id="af75343c-2f89-49a4-b73a-c736c64275ca" class="bulleted-list"><li style="list-style-type:disc">중앙 집중 시 요청 처리 메커니즘을 갖고 있다.</li></ul><ul id="7fc578bd-f53b-4867-969e-743ecc2ccdb6" class="bulleted-list"><li style="list-style-type:disc">스프링 웹 MVC 프레임워크의 DispatcherServlet(프런트 컨트롤러 역할)이 프런트 컨트롤러 패턴으로 구현돼 있음</li></ul><p id="4fac9ba3-7e7a-49d4-a916-88468b823e8c" class="">
</p><h2 id="d214d328-2c10-452b-ace5-38fe229e20fc" class="">Forward vs Redirect</h2><ul id="63d5874c-b7c9-4b03-9918-b4a4c5ee197f" class="bulleted-list"><li style="list-style-type:disc">Forward: 서블릿에서 클라이언트(웹 브라우저)를 거치지 않고 바로 다른 서블릿(또는 JSP)에게 요청하는 방식</li></ul><ul id="fa07df0d-5c59-49a7-91b1-5226e044ba9a" class="bulleted-list"><li style="list-style-type:disc">Forward 방식은 서버 내부에서 일어나는 요청이기 때문에 HttpServletRequest, HttpServletResponse 객체가 새롭게 생성되지 않음(공유됨)</li></ul><ul id="28153f57-6d2c-44c4-bd29-8fe494337548" class="bulleted-list"><li style="list-style-type:disc">RequestDispatcher dispatcher = request.getRequestDispatcher(”포워드 할 서블릿 또는 JSP”)</li></ul><ul id="792e9795-29fe-4aa5-937e-03c5b9bf0536" class="bulleted-list"><li style="list-style-type:disc">dispatcher.forward(request, response</li></ul><hr id="e9f8e7af-e2cb-4c69-a45d-a7a9ba08ea95"/><ul id="b6b02fc1-8bdc-4643-bc31-a7c1013eddf2" class="bulleted-list"><li style="list-style-type:disc">Redirect: 서블릿이 클라이언트(웹 브라우저)를 다시 거쳐 다른 서블릿(또는 JSP)에게 요청하는 방식</li></ul><ul id="8d1897a5-c1aa-4014-b2e4-d09f599c48b1" class="bulleted-list"><li style="list-style-type:disc">Redirect 방식은 클라이언트로부터 새로운 요청이기 때문에 새로운 HttpServletRequest, HttpServletResponse 객체가 생성됨</li></ul><ul id="edf438bc-fcd4-4ecf-add7-6df411eee152" class="bulleted-list"><li style="list-style-type:disc">HttpServletResponse 객체의 sendRedirect() 이용</li></ul><p id="5dbda553-6f86-4e2c-a602-af9da4972e15" class="">
</p><h2 id="e42ff46d-865b-4c76-ba5f-69ca9c09e78b" class="">MVC의 흐름</h2></a></figure><p id="20578ab2-c48f-485c-8f3a-38c5cc7f9187" class="">
</p><p id="b5785f6c-006c-4e32-8681-491cad1c3009" class="">
</p><h2 id="e6690e45-0dd6-4137-996e-b26091ad0743" class="">실습</h2><p id="e7ea0f4b-fdbe-439d-9845-2bbe5095df79" class="">mvc-practice2 프로젝트를 새로 생성하고, Main 파일의 이름을 WebApplicationServer로 설정해준다.</p><pre id="9ad06f2f-a769-4164-aa2d-4e89f3522f50" class="code"><code>dependencies {
    implementation &#x27;org.apache.tomcat.embed:tomcat-embed-core:8.5.42&#x27;
    implementation &#x27;org.apache.tomcat.embed:tomcat-embed-jasper:8.5.42&#x27;

    implementation &#x27;javax.servlet:javax.servlet-api:4.0.1&#x27;
    implementation &#x27;javax.servlet:jstl:1.2&#x27;

    implementation &#x27;ch.qos.logback:logback-classic:1.2.3&#x27;

    testImplementation &#x27;org.junit.jupiter:junit-jupiter-api:5.8.1&#x27;
    testRuntimeOnly &#x27;org.junit.jupiter:junit-jupiter-engine:5.8.1&#x27;
}</code></pre><p id="a690a15e-27dc-4505-b2dd-6acedcf5b3b2" class="">dependency는 다음과 같이 설정한다.</p><p id="6e80b039-d0f8-402e-80d8-e59425a859f3" class="">embeded tomcat이 실행되도록 다음과 같이 main 메서드를 구성한다.</p><pre id="b349b09e-4faa-49b7-8a6c-70f704525ec7" class="code"><code>package org.example;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class WebApplicationServer {
    private static final Logger log = LoggerFactory.getLogger(WebApplicationServer.class);

    public static void main(String[] args) {
        String webappDirLocation = &quot;webapps/&quot;;
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        tomcat.addWebapp(&quot;/&quot;, new File(webappDirLocation).getAbsolutePath());
        log.info(&quot;configuring app with basedir: {}&quot;, new File(&quot;./&quot; + webappDirLocation).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}</code></pre><p id="cb46ad2c-2297-47b5-84fe-3d782fd9c340" class="">아래는 정상적으로 Tomcat이 실행된 모습</p></a></figure><p id="43406665-5cd7-4e9f-973b-36f1acf90d3e" class="">tomcat에 의해 webapps&gt;WEB-INF&gt;classes&gt;org&gt;example&gt;에 WebApplicationServer가 만들어진 것을 확인하면 된다.</p></a></figure><p id="70715457-5016-45d8-8115-aaa7a47511bb" class="">그럼 이제 DispatcherServlet을 직접 간단하게 나마 구현해보겠다.</p><p id="bb6c50b5-0095-426b-836e-4f02f31c658a" class="">tomcat이 실행할 수 있도록  HttpServlet을 상속해준다.</p></a></figure><p id="0c2b6b09-03a4-47ee-8f49-816363c1976a" class="">그리고 @WebServlet 어노테이션을 붙여서 어떤 경로로 요청이 들어오더라도 DispatcherServlet이 받을 수 있게 해준다.</p></a></figure><p id="f1cb0b83-f38d-4662-8a65-edf13cf46aa6" class="">시험을 위해 다음과 같이 오버라이드 service 메서드와 그 안에 로그까지 야무지게 추가해준다.</p></a></figure><p id="1c2124d2-dd8f-436c-b307-05de2eb8595f" class="">참고로 resource 디렉토리에 미리 설정해둔 logback 설정파일도 추가해주었다.</p><pre id="ddfe7480-675d-43d5-a55d-c797a428af7d" class="code"><code>&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
&lt;!DOCTYPE configuration&gt;
&lt;configuration&gt;
    &lt;appender name=&quot;STDOUT&quot; class=&quot;ch.qos.logback.core.ConsoleAppender&quot;&gt;
        &lt;layout class=&quot;ch.qos.logback.classic.PatternLayout&quot;&gt;
            &lt;Pattern&gt;%d{HH:mm:ss.SSS} [%-5level] [%thread] [%logger{36}] - %m%n&lt;/Pattern&gt;
        &lt;/layout&gt;
    &lt;/appender&gt;

    &lt;logger name=&quot;org.example&quot; level=&quot;DEBUG&quot;/&gt;

    &lt;root level=&quot;INFO&quot;&gt;
        &lt;appender-ref ref=&quot;STDOUT&quot;/&gt;
    &lt;/root&gt;
&lt;/configuration&gt;</code></pre><p id="4e5b642c-83d5-48ee-8cc0-78ac0f1a9a30" class="">그렇게 재실행 시킨다음, localhost:8085를 입력하면</p><figure id="9032ec10-4994-4a65-b952-454e884d006f" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.22.02.png"><img style="width:3584px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.22.02.png"/></a></figure><p id="f726795b-d5e5-4288-ba1f-41dc452639a5" class="">브라우저상에는 아무것도 뜨질 않지만…</p><figure id="e5fb555f-3ecc-43e3-ab5b-3c7c37bbf7ea" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.22.32.png"><img style="width:2114px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.22.32.png"/></a></figure><p id="9770b457-7071-462b-82c6-251adc0fa34f" class="">이렇게 로그가 나오는 것을 볼 수 있다.</p><p id="4143a323-e232-421e-abe4-6fd524284b38" class="">위 로그는 어떤 경로로 요청이 들어오더라도 뜬다.(@WebServlet 때문에)</p><p id="0cadeecd-0a38-4564-9e45-2756f6026c10" class="">그 다음에는 controller를 빠르게 구현해본다.</p><figure id="46b91483-2b74-4023-8db6-df84ed8807b2" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.47.04.png"><img style="width:1774px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.47.04.png"/></a></figure><figure id="07aeafe2-1326-411d-80b6-5791bc5efa41" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.47.13.png"><img style="width:2010px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.47.13.png"/></a></figure><p id="d58e8431-b90d-4ce2-8f34-228c684f1f9c" class="">Controller와 HomeController를 만들고, HomeController에서 구현한 Controller의 handleRequest는 “home” view를 리턴하도록 한다.</p><figure id="54bb0b54-6c92-4386-8bae-d6d6c961cd13" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.58.59.png"><img style="width:1722px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_10.58.59.png"/></a></figure><p id="b034cb5e-2890-408f-aeff-c42b6b9965c3" class="">마찬가지로 controller 와 함께 RequestMappingHandlerMapping을 만들고</p><p id="ad86a8c1-9c4e-4ad3-afd5-a0f409812044" class="">아무 경로도 구체적으로 설정되지 않는다면 homeController가 매치될 수 있도록 mapping 해준다.</p><p id="30a96b25-e4aa-4346-b9ee-6032fa7de255" class="">이건 DispatcherServlet에서 사용될 건데</p><figure id="46aa0063-84db-4cf5-831b-89aaf97e006b" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.02.21.png"><img style="width:2324px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.02.21.png"/></a></figure><p id="1f1afdae-78ba-499f-baf2-0de4a6c3aa87" class="">요로코롬 RquestMappingHandlerMapping 참조변수를 하나 정의한 다음</p><p id="d6722517-abea-4550-abd7-119d644c2568" class="">오버라이드 init() 메서드를 통해 초기화해준다.</p><p id="784eaf69-b8c1-4c78-bea6-96b59ae34c32" class="">이 의미는 뭐냐면 톰캣이 httpServlet을 싱글톤으로 만드는데 그 때 init메서드가 호출된다.</p><p id="7c32e281-a443-4f15-93dd-fc364113f50b" class="">그때 서블릿이 만들어지면서 RequestMappingHanderMapping이 mapping을 초기화하도록 하는 것이다.</p><figure id="f2015169-7084-4a45-8e29-51503d9dec3c" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.05.06.png"><img style="width:2228px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.05.06.png"/></a></figure><p id="d65ede8c-6328-48b0-a5fa-d6a8fb6da5a2" class="">위에서 만든 rmhm객체에게 요청이 들어왔을 때 그 요청 urI를 처리할 수 있는 handler를 달라고 위임한다.</p><p id="c6c98e76-adaa-463b-8f1e-6164c6ad537d" class="">그럼 해당 handler에 적절한 handler가 들어온 다음은 controller에게 작업을 위임한다.</p><figure id="954857aa-02d6-4dc8-beea-1aad49e3899a" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.12.25.png"><img style="width:2098px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.12.25.png"/></a></figure><p id="3ace31c7-9243-45a2-8d90-b375a7e12a02" class="">viewName을 받으면 request객체의 getRequestDispatcher 메서드를 이용해서 적절한 RequestDispatcher를 얻은 다음, 해당 뷰로 request, response를 전달한다.</p><figure id="223d0daa-681e-4a21-9f71-7726b817757d" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.13.51.png"><img style="width:2070px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.13.51.png"/></a></figure><p id="fbf23dbc-3127-4e9e-88c7-aac529e0ae23" class="">webapps 밑에 home.jsp를 추가해주고</p><pre id="b71cea7a-23db-4187-8bf3-0187130a6afb" class="code"><code>&lt;%@ page contentType=&quot;text/html;charset=UTF-8&quot; language=&quot;java&quot; %&gt;
&lt;%@ taglib prefix=&quot;c&quot; uri=&quot;http://java.sun.com/jsp/jstl/core&quot; %&gt;

&lt;!DOCTYPE html&gt;
&lt;html lang=&quot;kr&quot;&gt;
&lt;head&gt;
    &lt;meta charset=&quot;UTF-8&quot;&gt;
    &lt;title&gt;home&lt;/title&gt;
&lt;/head&gt;
&lt;body&gt;
Home 페이지
&lt;/body&gt;
&lt;/html&gt;</code></pre><p id="16fb13aa-1b82-4229-8e03-eb391e5e8345" class="">실행시켜본다.</p><figure id="77b048a6-52a6-4f9b-b689-af8a5fea1c9a" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.17.58.png"><img style="width:3584px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_11.17.58.png"/></a></figure><p id="e22f5d9f-6552-4242-a4bf-1449e9b8a6f5" class="">제대로 ‘home페이지’ 라고 뜬 것을 볼 수 있다.</p><p id="a23e9bcc-e4b8-40d3-bfe0-1559e34feb8b" class="">
</p><p id="9f24921b-5d5b-41cd-8595-b29b414abb25" class="">그럼 이를 기반으로 MVC 프레임워크를 만들어보자</p><p id="2d0b2ad0-0cd3-48ea-ac22-833f99ed77ec" class="">일단은 Controller가 원래 1개밖에 없으니, 하나 더 만들어주자</p><p id="f2f8cf1f-e2c7-45f1-bdb8-f5dbf6539a79" class="">Controller를 상속받는 UserListController</p><pre id="f94b114f-63b5-4ba7-b01b-f6fd2c146b80" class="code"><code>package org.example.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserListController implements Controller {


    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return &quot;/user/List.jsp&quot;;
    }
}</code></pre><p id="aebe44f7-25ea-4858-b173-14fa3733658d" class="">그리고 webapps, 루트 디렉토리 밑에 list.jsp 추가</p><pre id="2adc2975-f85b-4b39-be59-fefbf4c27f1f" class="code"><code>&lt;%@ page contentType=&quot;text/html;charset=UTF-8&quot; language=&quot;java&quot; %&gt;
&lt;%@ taglib prefix=&quot;c&quot; uri=&quot;http://java.sun.com/jsp/jstl/core&quot; %&gt;

&lt;!DOCTYPE html&gt;
&lt;html lang=&quot;kr&quot;&gt;
&lt;head&gt;
    &lt;meta charset=&quot;UTF-8&quot;&gt;
    &lt;title&gt;list&lt;/title&gt;
&lt;/head&gt;
&lt;body&gt;

&lt;table&gt;
    &lt;thead&gt;
    &lt;tr&gt;
        &lt;th&gt;#&lt;/th&gt;
        &lt;th&gt;아이디&lt;/th&gt;
        &lt;th&gt;이름&lt;/th&gt;
        &lt;th&gt;&lt;/th&gt;
    &lt;/tr&gt;
    &lt;/thead&gt;
    &lt;tbody&gt;
    &lt;c:forEach items=&quot;${users}&quot; var=&quot;user&quot; varStatus=&quot;status&quot;&gt;
        &lt;tr&gt;
            &lt;th scope=&quot;row&quot;&gt;${status.count}&lt;/th&gt;
            &lt;td&gt;${user.userId}&lt;/td&gt;
            &lt;td&gt;${user.name}&lt;/td&gt;
            &lt;/td&gt;
        &lt;/tr&gt;
    &lt;/c:forEach&gt;
    &lt;/tbody&gt;
&lt;/table&gt;
&lt;/body&gt;
&lt;/html&gt;</code></pre><p id="35fb7b9c-196c-462d-9de2-c0a194628322" class="">list.jsp에서 users를 받아야 하기 때문에, users를 전달하는 부분도 필요하다.</p><figure id="d2a32a3c-0578-4c2b-878c-89b15f5a7dd4" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_1.45.13.png"><img style="width:1918px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_1.45.13.png"/></a></figure><p id="4e17b0eb-9180-42e2-ba19-b18197e90db7" class="">단순하게 request 객체를 통해서 줄 수도 있다.</p><figure id="d5e00fd8-6e8d-4483-b600-74a7b1d4fe97" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_1.46.38.png"><img style="width:1870px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_1.46.38.png"/></a></figure><p id="1d4c94f2-6aac-456f-b1a4-ff33a96c14d5" class="">RequestMappingHandlerMapping에서 mapping까지 해주면!</p><figure id="989e9bd1-d72c-4956-a36a-f07fdf58e17b" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_1.51.09.png"><img style="width:3584px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_1.51.09.png"/></a></figure><p id="46005494-9c57-4dce-a41c-3e61e6d273b0" class="">mapping에 의해 적절한 컨트롤러를 찾아 화면을 매칭시켜주는 코드가 완성된다.</p><p id="80296591-fa4d-44e0-ad1b-ac075ab19786" class="">하지만 지금껏 우리가 구현한 DispatcherServlet은 그야말로 누더기에 허점투성이이다.(슬프지만 사실인 걸 어쩌겠는가)</p><p id="9aad7bad-727f-42a8-b43b-75c4ccaf10b0" class="">단적인 예를 들어보자면 Users라는 경로로 get일 때는 </p><figure id="6f2e50f6-266e-4bf9-a494-22d4dc6f5f9e" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.09.43.png"><img style="width:1742px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.09.43.png"/></a></figure><p id="5bb15605-8ad5-4d6f-b735-4560607e3b18" class="">UserListController()가 호출되는 것이 맞다. 만약 post라면 UserCreateController()를 호출하고 싶을 때, 진짜 그럴 수 있을까?</p><p id="e5150256-1496-4da7-8bea-fe7a7c540ee7" class="">안타깝게도 위에 get방식으로 호출하는 것과 경로가 같기 때문에(key가 같기 떄문에) 불가능하다.</p><p id="fe5a5aa1-774b-44f3-9a88-fbaec1ac3420" class="">→get인지 post인지 구분 불가</p><p id="97f676bb-4830-412b-bc4e-c00d291d97da" class="">이를 구분시키기 위해서 Hamdlerkey라는 클래스를 정의한다.</p><p id="48d55b58-35d3-4c4c-bc50-6da0f7550176" class="">원래 dispatcherServlet이 HandlerMapping을 통해서 Controller 즉 핸들러를 선택하는데</p><p id="28ab2a7b-44fc-4e84-8869-a9956384f574" class="">지금 이 HandlerMapping역할을 RequestMappingHandlerMapping이 하고 있고, 경로에 따라서 Controller를 선택하고 있다.</p><figure id="6b9a20ef-a04b-463b-b406-e940bdb12d98" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.19.02.png"><img style="width:2146px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.19.02.png"/></a></figure><p id="8e656e4c-1f37-4557-a161-6eef7344eb51" class="">어쨌든 HandlerKey를 원래 경로를 지정해줬던 부분에 넣고</p><p id="9ee80008-fa37-44a8-bb5d-e5bfda022248" class="">HandlerKey클래스를 만들어준다.</p><p id="34071406-93cf-4804-a30b-a782621749d8" class="">그리고 HandlerKey 생성자에 들어갈 RequestMethod는 enum으로 만들어준다.</p><figure id="bbdf312d-9b76-4c98-a5b3-ec3f835d34af" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.20.30.png"><img style="width:888px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.20.30.png"/></a></figure><p id="b28ae975-aa9e-48a5-8152-fe4df0f91b8a" class="">아래는 HandlerKey구현</p><figure id="035af1f7-9f25-4bc1-b177-e2cbf4ae7d17" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.22.10.png"><img style="width:1402px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.22.10.png"/></a></figure><pre id="4c8429c0-496c-4f52-895a-49fae2ff60f3" class="code"><code>package org.example.mvc;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(&quot;/&quot;)
public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMappingHandlerMapping rmhm;

    @Override
    public void init() throws ServletException {
        rmhm = new RequestMappingHandlerMapping();
        rmhm.init();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info(&quot;[DispatcherServlet] #service started&quot;);
        Controller handler = rmhm.findHandler(new HandlerKey(RequestMethod.valueOf(request.getMethod()),request.getRequestURI()));
        try {
            String viewName = handler.handleRequest(request, response);

           RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
           requestDispatcher.forward(request, response);
        } catch (Exception e) {
            log.error(&quot;exception occur&quot;);
        }
    }
}</code></pre><pre id="f98bacdb-fc9d-4d43-b874-f3cff2e2a540" class="code"><code>package org.example.mvc;

import org.example.mvc.controller.*;

import java.util.HashMap;
import java.util.Map;
public class RequestMappingHandlerMapping {
    //
    private Map&lt;HandlerKey, Controller&gt; mappings = new HashMap&lt;&gt;();
    void init(){
        mappings.put(new HandlerKey(RequestMethod.GET,&quot;/&quot;), new HomeController()); // 어떠한 경로도 설정되지 않는다면 homeController가 실행
        mappings.put(new HandlerKey(RequestMethod.GET,&quot;/users&quot;), new UserListController());
       // mappings.put(new HandlerKey(RequestMethod.POST,&quot;/users&quot;), new UserCreateController());

    }

    public Controller findHandler(HandlerKey handlerKey){
        return mappings.get(handlerKey);
    }
}</code></pre><p id="ba5fe5b6-0f4b-4b34-b9e7-3c02aff7ae24" class="">인자가 바뀌었으니 DispatcherServlet에서도 service 메서드의 컨트롤러 즉 requestDispatcher를 가져오는 부분을 수정해준다.</p><p id="fd591294-2a0a-4cbe-81f7-fff54bf3aabc" class="">실행시켜보면 정상적으로 화면이 뜨는 것을 볼 수 있다!</p><figure id="873692d6-946e-4d3f-a962-b049b716ab2f" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.37.36.png"><img style="width:3022px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.37.36.png"/></a></figure><p id="5121a28d-dcae-4f93-83ff-3658ad983d2a" class="">제대로 실행되는 것을 확인했으니 이젠 UserCreateController를 구현해보자</p><pre id="01057227-6e3b-4468-9105-fa5320d47a58" class="code"><code>package org.example.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserCreateController implements Controller{


    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // User 추가해주는 코드
        return &quot;redirect:/users&quot;;
    }
}</code></pre><p id="61897ac5-eb38-4601-98cc-f24d0683fbc8" class="">handleRequest 메서드에서 user를 추가한다음, users로 redirect 해 줄 예정이라서</p><p id="a2567fb3-bf98-4f8c-8873-9576f3c041d4" class="">User 클래스를 mvc&gt;model&gt; 여기에 추가해준다.</p><p id="b1bba1c7-ac15-4040-ba46-c9d96a99c331" class="">그 다음에 repository 디렉토리를 만들고 이 안에 UserRepository 클래스를 만들어준다.</p><p id="5d90b3b2-4413-4c0c-9af7-577da4be9055" class="">이름과 같이 user를 보관하는 저장소 역할을 하고</p><p id="04b546c6-bcfa-4689-b2cb-3c9acf2d6245" class="">우리는 자료구조 HashMap을 통해서 이를 저장해줄 것이다.</p><pre id="6816720e-ae81-4a38-a0f7-547ca6325430" class="code"><code>package org.example.mvc.repository;

import org.example.mvc.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
  private static Map&lt;String, User&gt; users = new HashMap&lt;&gt;();


public static void save(User user) {
        users.put(user.getUserId(), user);

         //이렇게 만들어줬으니, user에도 getUserId() 메서드 만들어줘야겠지?!
    }
}</code></pre><p id="ae4cb9d4-cfad-4951-a1f6-dc7815b14109" class="">그러면 원래 유저 객체를 생성하고 추가해줘야하는 UserCreateController에다 해당 코드를 추가할 수 있다.</p><pre id="9bbf8834-c2df-4582-8ec2-1f844996dff9" class="code"><code>package org.example.mvc.controller;

import org.example.mvc.model.User;
import org.example.mvc.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserCreateController implements Controller {


    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // User 추가해주는 코드
        UserRepository.save(new User(request.getParameter(&quot;userId&quot;), request.getParameter(&quot;name&quot;)));
        return &quot;redirect:/users&quot;;
    }
}</code></pre><p id="80332001-ddb9-47dd-84fe-08ea48a8d989" class="">
</p><figure id="c619bd20-2f8c-45c4-94eb-1e3cc316b548" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.54.34.png"><img style="width:2138px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.54.34.png"/></a></figure><p id="33c44064-a5b9-4fb1-a9dc-66aadedb7187" class="">추가적으로 controller 디렉토리 밑에 ForwardController를 추가해줬는데 이게 무슨 의미냐면</p><figure id="b61d844e-1b2e-4aea-b401-d118f0b588cb" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.56.39.png"><img style="width:2518px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.56.39.png"/></a></figure><p id="0580e4bf-ffaf-4c17-8f63-078339c35e6f" class="">/user/form이라는 요청이 들어오면 단순히 /user/form으로 forward 해주는 역할이다.</p><p id="fe481648-d7ee-4b07-822f-523a3add66c6" class="">webapps&gt;user 밑에 form.jsp를 추가해주고 실행시켜본다.</p><pre id="6020ea97-425f-46b6-b2db-9328e5bed870" class="code"><code>&lt;%@ page contentType=&quot;text/html;charset=UTF-8&quot; language=&quot;java&quot; %&gt;
&lt;%@ taglib prefix=&quot;c&quot; uri=&quot;http://java.sun.com/jsp/jstl/core&quot; %&gt;

&lt;!DOCTYPE html&gt;
&lt;html lang=&quot;kr&quot;&gt;
&lt;head&gt;
    &lt;meta charset=&quot;UTF-8&quot;&gt;
    &lt;title&gt;form&lt;/title&gt;
&lt;/head&gt;
&lt;body&gt;
&lt;form method=&quot;post&quot; action=&quot;/users&quot;&gt;
    &lt;div&gt;
        &lt;label for=&quot;userId&quot;&gt;사용자 아이디&lt;/label&gt;
        &lt;input class=&quot;form-control&quot; id=&quot;userId&quot; name=&quot;userId&quot; placeholder=&quot;User ID&quot;&gt;
    &lt;/div&gt;
    &lt;div&gt;
        &lt;label for=&quot;name&quot;&gt;이름&lt;/label&gt;
        &lt;input class=&quot;form-control&quot; id=&quot;name&quot; name=&quot;name&quot; placeholder=&quot;Name&quot;&gt;
    &lt;/div&gt;
    &lt;button type=&quot;submit&quot;&gt;회원가입&lt;/button&gt;
&lt;/form&gt;
&lt;/body&gt;
&lt;/html&gt;</code></pre><p id="f54e5fec-1cf2-4ee3-b038-a628057332a6" class="">실행결과</p><figure id="b5cd901b-28ec-41b1-afe0-e95c09087071" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.02.35.png"><img style="width:2322px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.02.35.png"/></a></figure><p id="3cd21c3a-2d09-4a71-b010-17282e0e1b9e" class="">흐름을 따라가보자면, 회원가입 버튼을 누르는 순간 /users 로 post요청이 간다.</p><p id="b340f6d9-1ab5-495a-bf92-727704682416" class="">DispatcherServlet이 이 요청을 받아서</p><p id="c635ccc7-4dc6-4a70-b497-71f940523fd3" class=""><code>Controller handler = rmhm.findHandler(new HandlerKey(RequestMethod.</code><code><em>valueOf</em></code><code>(request.getMethod()),request.getRequestURI()));</code></p><p id="db7f02df-cf03-4fb4-ae9b-99e92572be8c" class=""> HandlerMapping (여기서는 RequestMappingHandlerMapping)에게 요청에 맞는 users에 해당하는 핸들러가 있냐라고 요청한다.</p><p id="bebf63b1-6cc0-497f-9c50-e3d248c64651" class="">그러면 HandlerMapping (여기서는 RequestMappingHandlerMapping)이 users에 해당하는 </p><figure id="67947b78-bbb7-4aa6-9a8b-35af17a8fae1" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.08.11.png"><img style="width:2258px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.08.11.png"/></a></figure><p id="af08d2d2-15d2-4c94-b17f-0190401773b0" class="">post 요청이면 UserCreateController()가 리턴될테니</p><p id="c8439dd1-4f3b-43f6-923d-5fbf4a60d836" class="">UserCreateController에서 HandleRequest() 메서드가 호출된다.</p><figure id="50686604-75c5-47c3-9899-9a7c105b4781" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.09.43.png"><img style="width:2200px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.09.43.png"/></a></figure><figure id="5c8ef080-9639-4da7-a8a1-009366fd4530" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.09.59.png"><img style="width:2058px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.09.59.png"/></a></figure><p id="7ae472a5-8bc5-43db-a67f-bf2cb50ad528" class="">User객체를 저장하고 users에게 redirect 해달라고 client에게 응답을 보낸다.</p><p id="e90dca1f-ecd3-4b83-bb4d-bbd0768d4685" class="">그러면 redirect는 get요청이니까 get요청으로 다시 들어오고</p><p id="a7662fa0-e6fe-4480-8c2c-91340112490b" class="">이를 dispatcherServlet이 받아서 HandlerMapping에게 물어보고</p><figure id="2bcbd722-e713-49da-9b49-a5588d9fe974" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.56.39.png"><img style="width:2518px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.56.39.png"/></a></figure><p id="f455a530-1063-47a3-b6f2-5c2b9bf33727" class="">이번에는 UserListController()가 리턴되고 유저의 리스트를 보여주는 </p><figure id="e2f57613-bed6-4a4d-8ab8-d267d08de6ec" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.12.12.png"><img style="width:1880px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.12.12.png"/></a></figure><p id="34f2dca1-9030-4b50-92f5-00e7067869a1" class="">List.jsp로 redirect되는 것이다.</p><p id="d941f9e0-12f4-44bd-94fc-98fd148c3dba" class="">그런데 505에러….순간 띠용했지만</p><figure id="c0413a63-8e98-4369-a81f-ef41cca75e94" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.13.48.png"><img style="width:2054px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.13.48.png"/></a></figure><p id="8afafa8a-800b-4bb5-a78d-ff82a375955f" class="">문제는 UserCreateController에 있었다.</p><p id="b5872196-3191-46ec-a39d-c89f14a64d63" class="">redirect:/users를 리턴해주는데</p><figure id="275fb84b-8b74-45dd-88f5-d9bdb7e672c9" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.14.48.png"><img style="width:2268px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.14.48.png"/></a></figure><p id="9449010d-996a-428a-aeb5-dbec4d446d0f" class="">DispatcherServlet에서 이를 viewName으로 받고, forward 시켜버리니까 안되는거였다!!</p><p id="ce2ebd3a-aab2-48cc-9789-fca95062e591" class="">redirect로 forward 시켜주는 게 가당키나 한가</p><p id="5bc663f0-cc4d-44b6-a3d8-081b534c9f36" class="">그래서 redirect일 경우에는 redirect 할 수 있도록 하고 forward일 때는 forward 할 수 있도록 분리를 해야한다.</p><p id="9d49aff9-9162-4e4d-b210-e12ee85949dd" class="">길을 잃었을 땐 언제나 초심으로 돌아가야 하는 법</p><figure id="c1d096df-d45f-4697-9b8a-d5b00eedbe8a" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.21.14.png"><img style="width:1244px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.21.14.png"/></a></figure><p id="f5c304a0-8983-4b8b-9fb6-ccf298451973" class="">원래 mvc 프레임워크를 보고 비교해보자면 우리가 구현한 mvc는 HandlerMapping을 통해 controller를 받아오지만 HandlerAdapter를 통하지 않고 이를 바로 실행시키고 있다.</p><p id="73f41f33-d4f3-4893-bd3e-cca81a6645f8" class="">그리고 ViewResolver도 없다</p><p id="a6c1576b-e105-4d3c-9eef-7cae08030037" class="">얘를 통해서 view가 redirect 뷰인지 jsp 뷰인지 판단할 수 있을텐데, 이게 없네;;</p><p id="03d7c420-41c0-4c41-bf2b-1790dd6f9222" class="">우선은 급한 ViewResolver를 먼저 만들기로 하자</p><p id="4c73bc32-643e-4c6e-af58-345c3e3c3abc" class="">mvc&gt;view 디렉토리를 만들어주고, View interface를 안에 생성해준다. (다형성 이용)</p><figure id="1066fae0-a6ca-492e-a0db-d4cbcede6676" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.27.23.png"><img style="width:1952px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.27.23.png"/></a></figure><p id="6bdd48d9-0a53-419d-a31e-061430e41e83" class="">View를 implements 한 JspView도 만들어주는데, 이 JspView의 render 메서드 안에 문제의 ‘그 코드’를 넣어준다.</p><p id="1bc88b9e-1dc4-4b31-b51d-8f615d13e40b" class="">즉, 화면 forward하는 부분을 JspView로 옮겨준 것이다.</p><figure id="eae7e87f-5116-4536-86c4-fe6578cf4678" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.34.12.png"><img style="width:2128px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.34.12.png"/></a></figure><p id="749ce671-cb6e-427c-82d3-a7fb81c6208c" class="">forward 역할을 만들었으니, 이제 redirect 역할인 RedirectView도 만들어보자</p><p id="c243aef5-67d5-4bb2-a1d2-e2d3b5021b62" class="">response.sendRedirect(name.substring(”redirect:”.length());</p><p id="a8d759df-9580-45eb-b033-9e97d620c7fc" class="">이걸 “redirect:” 이것만 상수로 뺴준다.</p><blockquote id="7afa768c-7a79-469f-b925-5114b7a1d85e" class="block-color-gray_background"><strong>COMMAND + OPTION + C 상수로 빼기</strong></blockquote><p id="bf49dee9-41bd-4435-af3f-11397dc90a10" class="">sendRedirect에서 자체적으로 redirect를 붙여주기 때문에 잘라주는 것이다</p><pre id="1e332e60-88f7-4a54-bfa3-75c8dd26545f" class="code"><code>package org.example.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View{
    public static final String DEFAULT_REDIRECT_PREFIX = &quot;redirect&quot;;
    private final String name;

    public RedirectView(String name) {
        this.name = name;
    }

    @Override
    public void render(Map&lt;String, ?&gt; model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(name.substring((DEFAULT_REDIRECT_PREFIX + &quot;:&quot;).length()));
    }
}</code></pre><pre id="c4c3e16c-1f8f-470a-a817-de4a679d1bae" class="code"><code>package org.example.mvc.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View{
    private final String name;

    public JspView(String name) {
        this.name = name;
    }


    @Override
    public void render(Map&lt;String, ?&gt; model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach(request::setAttribute);// model에 값이 있다면 request.setAttribute로 모두 setting 해주세요!
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(name);
        ((RequestDispatcher) requestDispatcher).forward(request, response);
    }
}</code></pre><p id="0fcdff88-ba94-412b-a9d6-625ba9f40fb3" class="">이렇게 두 forward와 redirect 뷰가 완성이됐고</p><p id="8f674d0d-6aa7-4263-bf4b-38d6981d10f8" class="">최종적으로 만들어야하는 대상은 ViewResolver였다.</p><p id="288ca60b-6c6b-45ab-b87d-b5bb06af4808" class="">view 디렉토리 안에 ViewResolver(인터페이스)를 만들어준다.</p><p id="e3e54388-4098-43b4-b10e-0c79d31154fe" class="">ViewResolver의 간략한 역할은 viewName을 받아서 뷰를 결정해주는 것이다.</p><figure id="e6c2a61c-afe1-41c5-90bb-7cd40b3964f8" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.43.20.png"><img style="width:796px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.43.20.png"/></a></figure><p id="c51565bf-0ab0-4d0b-91b3-2ec69573c403" class="">얘도 View의 경우와 마찬가지로 JSPViewResolver와 ForwardViewResolver를 구현해 줄 것이다.</p><pre id="b52b9696-fb88-42e7-bdcc-7cb2c21b6acc" class="code"><code>package org.example.mvc.view;

import static org.example.mvc.view.RedirectView.DEFAULT_REDIRECT_PREFIX;

public class JSPViewResolver implements ViewResolver{
    @Override
    public View resolveView(String viewName) {
        if(viewName.startsWith(DEFAULT_REDIRECT_PREFIX)){
            return new RedirectView(viewName);
        }
        return new JspView(viewName + &quot;.jsp&quot;);
    }
}</code></pre><p id="ce7ba064-bda2-403f-a4d9-5ce63b0faa28" class="">viewName이 redirect: 로 시작한다면 RedirectView로 보여줄 것이고 아니라면 JspView로 보여줄 것이다.</p><p id="47ece801-a9c7-44b8-8278-2f911207d980" class="">얘는 어떻게 쓰면 되냐면 DispatcherServlet에서 </p><p id="72e7ec66-7725-40b7-877a-f042929f01bd" class="">ViewResolver로 이루어진 List를 생성하고 이를 init()에서 </p><p id="4ad900c6-a549-4bc9-a953-a4e6b0d4863b" class="">Collections.singletonList(new JspViewResolver()); 로 초기화해준다.</p><p id="2ba36af0-b4c8-4d83-96e5-a07dd623c990" class="">-일단 viewResolver 중 하나만 등록…</p><pre id="e97457b4-085f-4d62-b5c2-25cf6df61491" class="code"><code>package org.example.mvc;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.RequestMethod;
import org.example.mvc.view.JSPViewResolver;
import org.example.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.mvc.view.View;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@WebServlet(&quot;/&quot;)
public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMappingHandlerMapping rmhm;

    private List&lt;ViewResolver&gt; viewResolvers;

    @Override
    public void init() throws ServletException {
        rmhm = new RequestMappingHandlerMapping();
        rmhm.init();
        viewResolvers = Collections.singletonList(new JSPViewResolver());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info(&quot;[DispatcherServlet] #service started&quot;);
        Controller handler = rmhm.findHandler(new HandlerKey(RequestMethod.valueOf(request.getMethod()),request.getRequestURI()));
        try {

            //redirect:/users
            String viewName = handler.handleRequest(request, response);

            for (ViewResolver viewResolver : viewResolvers) {
               View view = viewResolver.resolveView(viewName);
                view.render(new HashMap&lt;&gt;(), request, response);
            }


          /** RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
           requestDispatcher.forward(request, response);*/
        } catch (Exception e) {
            log.error(&quot;exception occur: [{}]&quot;, e.getMessage(), e);
            throw new ServletException(e);
        }
    }
}</code></pre><p id="58844f72-fd18-46a7-b821-7cb1db378f0e" class="">viewResolvers에서 돌면서, 뷰 이름을 기반으로 view를 가져오고 그 view에 맞는 render를 호출</p><p id="060ecce5-e46a-47c6-93aa-f8df334f3fd5" class="">render 메서드에서 model은 일단 new HashMap&lt;&gt;()으로 받는다.</p><p id="5a577b64-fb87-4ad9-9cb1-96dcde852692" class="">선택된 view가 redirect면 RedirectView에서의 render를 하고, forward면 JspView에서의 render를 하는 것이다!</p><figure id="71d9075e-09c5-474a-836b-33d34b3b3f22" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.02.08.png"><img style="width:2378px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.02.08.png"/></a></figure><p id="ea5e299d-9459-47b6-a90f-efde36678619" class="">실행시켜보면 이렇게 잘 나온다!!</p><figure id="4dd54cd1-9db5-4871-9f6d-ed6411609419"><div class="source"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2592%25E1%2585%25AA%25E1%2584%2586%25E1%2585%25A7%25E1%2586%25AB_%25E1%2584%2580%25E1%2585%25B5%25E1%2584%2585%25E1%2585%25A9%25E1%2586%25A8_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.05.04.mov">https://s3-us-west-2.amazonaws.com/secure.notion-static.com/06a31449-2763-4270-b9af-7534808fa5ad/%E1%84%92%E1%85%AA%E1%84%86%E1%85%A7%E1%86%AB_%E1%84%80%E1%85%B5%E1%84%85%E1%85%A9%E1%86%A8_2023-06-29_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_10.05.04.mov</a></div></figure><pre id="0ba7f3a5-0f8f-4d40-a08d-7a598a3875c0" class="code"><code>package org.example.mvc.repository;

import org.example.mvc.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static Map&lt;String, User&gt; users = new HashMap&lt;&gt;();

    public static void save(User user) {
        users.put(user.getUserId(), user);


    }

    public static Collection&lt;User&gt; findAll() {
        return users.values();

    }
}</code></pre><p id="183e16ea-998c-4354-8f7d-340e74d62ea0" class="">원래 findAll 이렇게 구현하면 안되긴 하나 간단하게 만들기 위해서 구현했다.</p><figure id="d3f7eb33-c123-48bf-a248-24d56e1fdf81" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.08.23.png"><img style="width:2086px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.08.23.png"/></a></figure><p id="f0e94dac-e219-4235-afe4-80da6ecbce19" class="">다시 한 번 서버를 띄우면…</p><figure id="91b80835-ca89-4325-9548-2806051ecbb3" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.09.16.png"><img style="width:2886px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.09.16.png"/></a></figure><p id="e3241a54-1f0c-4c32-b420-8a37aa7004d6" class="">어라…? 깨지네</p><p id="bb456983-3ce1-4b9f-a84b-e27ca200f247" class="">왜 깨질까</p><p id="5d0cf780-7eca-462d-a055-aad5a2dc0261" class="">프로퍼티 네임이 발견되지 않습니다라고 되어있는데…</p><p id="8c825aa1-9395-4f8e-b571-3350dd222792" class="">원인은 list에서 name도 받아오는데, 이를 위한 getter가 user클래스에 존재하지 않는다.</p><figure id="81c2a43a-9d3a-49ca-9ded-0f4b9aced8d4" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.11.22.png"><img style="width:1348px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.11.22.png"/></a></figure><p id="abda7a3a-80b9-4a3d-8e3d-0eaca6528b87" class="">깨지긴 하지만, 정상적으로 들어온 것을 볼 수 있다.</p><figure id="90b0c9c5-33f0-439b-abcd-63aa9837bfe6" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.11.53.png"><img style="width:1406px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.11.53.png"/></a></figure><p id="101c3e11-6f10-4bb7-a1e6-a34b7229d743" class="">한글이 깨지는 문제는 servlet에 filter를 이용해서 이 부분을 수정해주겠다.</p><p id="a6dd363e-a0f4-4067-8674-15f095e11de8" class="">mvc&gt;filter&gt;CharacterEncodingFilter 파일을 만들어주고</p><pre id="f046030f-3aea-4217-ada0-2f6b0d0dfd3d" class="code"><code>import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(&quot;/*&quot;)
public class CharacterEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(&quot;UTF-8&quot;);
        response.setCharacterEncoding(&quot;UTF-8&quot;);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}</code></pre><p id="219bc51b-1ba6-420c-9220-8be43de49109" class="">doFilter에다 request에 대한 encoding을 지정해주면 해결된다!!</p><figure id="19e3b862-db6d-486f-a84c-af01040db780" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.17.13.png"><img style="width:762px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.17.13.png"/></a></figure><p id="58a1bf64-7820-494d-aa87-84cfb761afd1" class="">user/form 들어가서 회원가입해주고…</p><figure id="1e07d8dd-1428-440b-b672-645b6a4db9c4" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.17.37.png"><img style="width:640px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.17.37.png"/></a></figure><p id="2867b042-83f7-4b6e-8410-28cd1b89697a" class="">한글도 정상적으로 나온다!</p><p id="5ab6ed9e-bba3-497b-bcf0-943d816304a9" class="">이제 구현 안된 거는 handler Adapter 뿐이다!</p><p id="6d5bd042-aa49-4dc3-9b23-1fec3427aa7a" class="">Handler Adapter를 구현해보자</p><p id="99b4f99e-71b3-4240-850d-f72fdd80de13" class="">mvc 디렉토리 밑에 HandlerAdapter 인터페이스를 만들어준다.</p><p id="b59d4c16-e753-4777-957d-8f099432233b" class="">Handler Adapter를 어떻게 활용해줄꺼냐면</p><p id="1afb6c2d-794e-4fba-9dfc-d89afa164be9" class=""><code>private List&lt;HandlerAdapter&gt; handlerAdapters;</code></p><p id="09ecdfe8-e4a7-4bd0-a224-16350e05f1dd" class="">DispatcherServlet에 HandlerAdapter로 구성된 리스트를 정의해주고</p><p id="6e3ad0e4-3821-46f0-bb6e-58be0b1fd5be" class=""><code>handlerAdapters = List.</code><code><em>of</em></code><code>(new SimpleControllerHandlerAdapter());</code></p><p id="0103d0ba-1b08-4e05-89a7-3f04786be491" class="">init() 함수안에 SimpleControllerHandlerAdapter를 등록해준다.</p><p id="f0063f2d-fb7f-4a8a-9c08-796f6ddac250" class="">얘는 무슨 역할을 하냐면</p><figure id="75316251-65d3-4ae8-967c-9e217eda8e47" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.34.16.png"><img style="width:1758px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.34.16.png"/></a></figure><p id="e9724bff-0241-4837-86a8-ffae16c58b56" class="">전달된 handler를 지원하는 adapter이냐? 라는 의미고</p><p id="03c2dad6-b140-49ca-8fc5-fa68a23363a8" class="">만약 지원한다면 handle 메서드로 수행한다.</p><p id="010b62eb-8bc5-49a6-946b-e4168665f2d5" class="">handle 메서드의 리턴 값은 ModelAndView </p><p id="196cefc3-5c59-460c-80ab-530fd72a0d56" class="">남궁성 선생님 수업시간에 배운 그 ModelAndView 맞다</p><p id="aa0ba013-9dd2-4c63-aece-f98778eab3d5" class="">하지만 여기에서는 간략하게나마 구현해보겠다.</p><p id="9b9fa5a7-4198-47fb-b523-4cf79093c6f9" class="">view 디렉토리 밑에 ModelAndView 클래스를 만들어 빨간줄을 없앤다.</p><figure id="29656aa2-ee62-4eae-aecc-d6e3b4c6e21f" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.34.39.png"><img style="width:1984px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.34.39.png"/></a></figure><p id="67e14627-a91e-44a5-bcf4-999dd7002d27" class="">그 다음에는 SimpleControllerHandlerAdapter를 구현해볼건데</p><figure id="650d1370-0d8b-4c4c-ae1d-0465dcbae523" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.35.49.png"><img style="width:978px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.35.49.png"/></a></figure><p id="ec82abed-5bb1-45dd-943f-49855b5db25d" class="">전달된 handler가 Controller를 구현한 구현체라면 지원 가능하다! 라는 의미</p><p id="4f87175d-ef9b-4b56-8266-d3630d6f13d4" class="">handle 메서드 인자에 Object handler 추가해주기!</p><p id="8f75ae38-7a5a-4dfe-986e-548d39461d8d" class="">handler를 Controller로 타입캐스팅 해주고 handleRequest를 실행한 다음에 request, response를 던지면</p><p id="9089e60e-e9f9-47cb-a40f-4a7ca926fadd" class="">viewName이 나오겠지…</p><pre id="db347bf4-bd76-4ba5-8e01-d00e0f71b3a0" class="code"><code>import org.example.mvc.controller.Controller;
import org.example.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String viewName = ((Controller) handler).handleRequest(request, response);
        return new ModelAndView(viewName);
    }
}</code></pre><p id="5f1283e3-92e5-46e0-8aa8-252cc2cff265" class="">저렇게 Controller를 구현한 녀석들은 handlerRequest의 리턴이 viewName이다!</p><p id="2d1b2655-f8dc-4f75-8403-f7bd746e4438" class="">new ModelAndView해서 viewName을 전달할 것이다.</p><p id="c1b9eefc-8d38-495b-8b60-736d856c2dd6" class="">즉 최종적으로는 객체로 감싸서 리턴할 수 있도록!!</p><p id="9aaf1801-c9b7-4ad9-98b9-02780de3bc4d" class="">그럼 ModelAndView에 viewName이 들어갈 수 있는 생성자를 만들어줘야 하는데</p><figure id="6f13ba44-c8ad-4444-bbf5-c5ec9dc75f67" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.43.26.png"><img style="width:952px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.43.26.png"/></a></figure><p id="fcf328fa-f22a-424d-a594-7f538b1ad2dc" class="">이렇게 구성해준다.</p><p id="6202387a-0ab2-4578-814b-3a945f194d4e" class="">이제 HandlerAdapter가 구현이 됐기 때문에</p><figure id="4294a537-6c9a-4857-b78f-ffa02d26a43e" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.50.57.png"><img style="width:2252px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.50.57.png"/></a></figure><p id="4c45e8b6-18fe-449b-9e74-6d52b3f44db7" class="">handlerAdapters에서 handlerAdapter를 가져온다음(filter를 통해 위에서 전달받은 handler를 지원하는지 확인, 지원하면 찾아서 HandlerAdapter리턴)</p><p id="c8e3b218-abf2-44b8-8cbf-b33619035642" class="">찾은 HandlerAdpater를 기반으로 handle을 사용해서 modelAndView리턴</p><p id="e8a32fe3-1275-46f2-a451-1b403bbab85a" class="">이 modelAndView를 갖고 아까 애매하게 채워주었던 model 부분을 대체한다.</p><figure id="742dddc7-6afe-4e08-839a-b02006c8b234" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.57.50.png"><img style="width:2000px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.57.50.png"/></a></figure><figure id="a861faf9-12a2-49ab-8a28-fdf7921401b2" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.57.36.png"><img style="width:1142px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_10.57.36.png"/></a></figure><p id="559dcb12-27a5-4da6-926a-89901f1acf93" class="">ModelAndView에서 모델을 얻을 수 있는 메서드도 추가!!</p><p id="1c43a81a-8cbb-4fde-a633-18ebff1d72e5" class="">기존의 handle.handleRequest(request, response) 이 부분도 삭제!!</p><p id="c2cf116e-cf74-46d0-b75b-450714cce697" class="">왜냐하면 이제는 DispatcherServlet에 요청이 들어오면 findHandler를 통해 handlerMapping을 먼저해서 handler를 받아오고, handlerAdapter를 통해 controller를 실행하기 때문에 아까 저걸 지울 수 있는거였고</p><p id="3d09ea80-42ef-4a38-b5ab-414eb948359a" class="">그 다음엔 선택된 handler를 실행시킨 handlerApdater가 modelAndView를 반납하면 </p><figure id="1a6af733-ab61-4603-b844-a7c9d85db300" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.04.59.png"><img style="width:1272px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.04.59.png"/></a></figure><p id="2e2e343d-7bb1-44a6-8f2a-08277b18d4b3" class="">이렇게 modelAndView를 통해 뷰 이름을 받아와서 viewResolver로 뷰를 받아오고</p><p id="be34ee90-8da8-4b1f-b45b-761f7422fcc3" class="">그 뷰를 render 해서 화면을 표시해주면 끝이다!</p><p id="ad1c1a83-36e3-49b3-80ca-5ad803236f3b" class="">실행도 정상적으로 되는 것을 확인할 수 있다!!</p><figure id="1faa4c45-3e26-4b65-9f1f-a17030cd490a" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.07.54.png"><img style="width:1566px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.07.54.png"/></a></figure><p id="83160f04-ef7d-41e5-ab84-66786b4cde43" class="">이 다음부터는 그저 refactoring 영역이다.</p><p id="1c2941a7-1d21-41bb-b99e-fd353a39057b" class="">RequestMappingHandlerMapping 이녀석을 inteface로 빼보거나</p><figure id="b33003c7-5f2e-4ebc-bdc0-4948794cbe29" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.11.21.png"><img style="width:1444px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.11.21.png"/></a></figure><p id="08d85b7e-5124-4e34-821e-c8cbdd797084" class="">이렇게 기존 rmhm을 인터페이스 타입으로 선언함으로써 구체적인 클래스 타입으로 선언하지 않는다</p><figure id="af10332f-08b5-40fc-ab91-45783e42b1c4" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.14.05.png"><img style="width:940px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.14.05.png"/></a></figure><p id="d1ff3647-5ca4-473f-811e-1e622685028c" class="">HandlerMapping 인터페이스를 만들 땐 findHandlerd의 리턴값을 Object로 만드는데</p><p id="5f9cd901-d9ca-4e5c-9fef-2774e96b3e1d" class="">이는 기존의 controller가 인터페이스 형태로 되어있어서 원래는 Controller를 리턴하게 했는데 이제는 Controller 인터페이스 형태가 아니라 annotation형태로도 받을 수 있도록 만들기 위해 object로 만든것이다.</p><figure id="19344cb8-151f-4442-9079-0302ff280e46" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.16.19.png"><img style="width:2142px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.16.19.png"/></a></figure><p id="88ff5c55-906a-47de-bf0f-5de201b31c59" class="">이렇게 바꿔도 정상적으로 실행이 될 것이다</p><figure id="ab285626-9367-4f40-a5a2-16f425a05896" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.16.54.png"><img style="width:756px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.16.54.png"/></a></figure><p id="8a48e45b-0f32-4751-96ba-421c5aba4ca4" class="">
</p><p id="ebf48339-0c19-4e35-8071-110c5f92a2ac" class="">
</p><p id="b849a3a7-ea3f-4004-ba52-c9a3c7c06ace" class="">만약 controller를 annotation 형태로 구현해달라는 요청이 들어온다면</p><p id="d5f82be4-1db2-4af4-ab09-fadffea1c1d1" class="">이렇게 AnnotationHandlerMapping을 HandlerMapping을 구현한 형태로 만들어 줄 것이다.</p><figure id="4606d4ac-41d4-4ca0-96d4-986c0ad3882c" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.18.43.png"><img style="width:1522px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.18.43.png"/></a></figure><p id="049b966e-3e65-492c-bb82-cf3667c9c684" class=""> DispatcherServlet에서 기존 hm을 HandlerMapping의 리스트로 받도록 수정해준다.<div class="indented"><blockquote id="9e3d4d16-7c23-4572-820b-cb86d5dd1e9c" class="block-color-gray_background">Shift + f6 이름바꾸기</blockquote></div></p><p id="bcc1ab2a-b18e-4f10-81f8-05d65dc95a19" class="">얘를 리스트로 받는 이유는 </p><figure id="1b9d52df-de6d-4902-861b-3598629300f1" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.22.00.png"><img style="width:1426px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.22.00.png"/></a></figure><p id="19216d3a-adde-448d-98de-b7813e479995" class="">이렇게 해주기 위해서이다.</p><p id="6ded146c-3cd8-4586-ab6d-5ce3c0518d42" class="">즉</p><figure id="b40dbe35-01ee-4b0d-a066-e13fd218c959" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.22.45.png"><img style="width:1858px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.22.45.png"/></a></figure><p id="de3cc52b-382b-465b-8940-3a7923302989" class="">기존의 이녀석을</p><figure id="6019f130-1045-4711-bcc1-44b2a9e1b641" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.23.09.png"><img src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.23.09.png"/></a></figure><p id="fe74097d-784b-47ee-84e6-73dca4827455" class="">이렇게 하겠다는 다짐!!</p><p id="d50f833b-8eca-4c83-bf00-3ba3c3b5b90a" class="">mvc&gt;annotation 디렉토리를 만들고, 이 밑에 Controller(Annotation) 파일을 만들어준다.</p><figure id="b7de3fbe-42a6-48ac-826c-20f5e7392eca" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.26.33.png"><img style="width:914px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.26.33.png"/></a></figure><p id="359a2903-ae3c-4964-86bb-2aedae704826" class="">Controller annotation을 클래스에 붙일 수 있도록 target을 설정해주고, 유지기간도 맞춰준다.</p><figure id="91cb042b-c438-4ce1-82c8-c0f08a765906" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.27.45.png"><img style="width:2108px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.27.45.png"/></a></figure><p id="7f450fb6-9317-430e-89c4-48eae27449a3" class="">그러면 이제는 인터페이스 형태가 아니라 완벽한 annotation 형태로 변화된 것을 볼 수 있당.</p><p id="eccce147-fa16-49b3-94bc-aab5c0338cca" class="">@RequestMapping을 사용하기 위해 얘도 직접 구현해준다.</p><figure id="0b90a0db-edd0-4ff5-8b23-39d6085663b0" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.30.32.png"><img style="width:1068px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.30.32.png"/></a></figure><p id="e430041f-22f9-4409-8a46-037be667e39c" class="">이렇게 annotation 형태로 붙여놓으면 알아서 handler가 찾을 수 있도록 구현한다!!</p><figure id="1530e751-63cf-421b-b3ec-f4b93842c465" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.31.57.png"><img style="width:1928px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.31.57.png"/></a></figure><p id="f7e09b1b-cdc4-46be-8602-872c89a96647" class="">그러기 위해서는 AnnotationHandlerMapping이 필요하다</p><figure id="be4048f2-c103-459e-8248-80e042cfe814" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.37.15.png"><img style="width:1430px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.37.15.png"/></a></figure><p id="3a5183ba-7ecc-4bd6-9b6d-4e63d94be998" class="">AnnotationHandlerMapping에서는 어떻게든 맵을 초기화하고 HandlerKey에 해당하는 어노테이션 핸들러를 리턴만 해주면 알아서 그 이후부터는 dispatcher서블릿에서 해결해 줄 것이다.</p><p id="7ecbdf8a-00ad-498e-9692-2a674e24b94f" class="">기존에는 하나였는데 지금은 리스트 형태이기 때문에 DispatcherServlet에서도 handlerMapping 부분을 수정해줘야한다. </p><figure id="fd0d97cf-92f4-4856-935b-bfe06a0c6959" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.43.18.png"><img style="width:2068px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.43.18.png"/></a></figure><figure id="a886a096-0bda-4720-a675-3bb678878e17" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.51.10.png"><img style="width:1410px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.51.10.png"/></a></figure><p id="96959882-1baa-4d2a-b381-1d0523804b12" class=""> AnnotationHandlerMapping은 다음과 같이 구현하는데</p><p id="99c4b245-66b4-4a16-9bc0-f0b45781ad21" class="">basePackage가 왜 필요하냐면 </p><figure id="42e9f34b-3203-44f6-80fc-6a381ce95207" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.52.25.png"><img style="width:1790px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.52.25.png"/></a></figure><p id="39569f74-d5ae-4bdd-930c-65f73d691f6a" class="">이렇게 basePackage 지정해주려고</p><p id="013a74b1-4b2e-486d-9a01-6482710bd393" class="">그 다음에 초기화 해주는 메서드 하나 만들기</p><figure id="cb378889-3b76-486a-90ef-eaf703541579" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.54.07.png"><img style="width:1654px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.54.07.png"/></a></figure><p id="6ef984d2-4aec-44fe-a8a7-5f231a924f00" class="">위의 rmhm.init()과 같은 역할을 한다.(mapping 즉 맵을 초기화)</p><p id="7c04e293-52c4-42c2-91d0-0382393c08d0" class="">이를 reflection을 통해서 할건데, 그래서 reflection dependency추가</p><figure id="b37d863e-d97c-4afc-b9d9-38ebb06a5d38" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.55.57.png"><img style="width:1320px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.55.57.png"/></a></figure><p id="dcecd10d-23d6-4511-95b5-13e5372c2c91" class="">이렇게 하면 basePackage 밑에 Controller 어노테이션이 붙어있는 클래스들을 전부 가져오는 것이다.</p><figure id="a7164d5c-4ed2-44ad-b440-fff26acb5815" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.57.42.png"><img style="width:1634px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_11.57.42.png"/></a></figure><p id="17e10665-4e11-42e1-a194-fb5423266ddc" class="">지금은 homeController 밖에 붙어있지 않기 때문에 homeController에 대한 클래스 타입 객체가 </p><p id="f2ce6498-6e3a-499b-bef2-52b6433cfe1f" class="">homeController만을 가져온다.</p><figure id="4f62a57e-5816-4aac-8e0a-da361c5a9011" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-30_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_12.13.45.png"><img style="width:2304px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-30_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_12.13.45.png"/></a></figure><p id="89abcd52-ae30-4c9b-a09e-a81b0b6ad13c" class="">다시 정리하자면 컨트롤러 어노테이션이 붙은 클래스 타입 객체를 다 가져오고 </p><p id="a7bb77f7-6812-458d-9c6e-1720a34441ef" class="">클래스 타입 객체 안에 메서드에 requestMapping이 붙은 녀석들을 다 가져온다.</p><p id="127cf8a2-7fe2-4965-918f-1865e832ffc3" class="">그 requestMapping 값을 다 가져와서</p><p id="f1e25960-1e59-4f31-8a7c-0796245138fa" class="">HandlerKey를 만들어주는데 여기서는 어차피 homecontrolle라서 method는 GET이 들어가고 value는 “/”가 들어간다.</p><figure id="5be29ae7-4f74-40f6-b457-0e4d2b6757e9" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-30_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_12.21.17.png"><img style="width:2100px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-30_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_12.21.17.png"/></a></figure><p id="68cf2132-ec52-4130-85f7-d3f70a2b1dee" class="">AnnotationHandler까지 만들어줬으면 흐름을 살펴보자</p><figure id="1d1d7a1e-fab2-42c4-a32c-030a2a6b2606" class="image"><a href="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-30_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_12.22.12.png"><img style="width:2528px" src="MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2034d2ebfbbe464d0fa0ab6813c27a6887/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-06-30_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_12.22.12.png"/></a></figure><p id="65af5bdf-f472-4bd9-a3c0-84922b2f45c9" class="">AnnotationHandlerMapping에서 초기화 해줄 때 new HandlerKey(requestMethod, requestMapping.value())와 일치하는 핸들러는 클래스 타입 (clazz) declaredMethod를 실행해줘야 한다.</p><p id="4473c480-105e-49e7-8ea5-d57746f37d4f" class="">그래서 이 클래스 정보와 메서드 정보를 가진 어노테이션 핸들러를 하나 만든다.</p><p id="6fb2079b-f9ab-47a8-a038-1493b3b13e64" class="">그런다음 핸들 메서드가 호출되는 순간 메서드를 호출하는 것이다(invoke)</p><p id="dc53ab91-7153-4d55-932b-8ba823b8aa83" class="">
</p><p id="da268cc5-a201-4a85-8a97-24b27e0d30b5" class="">마지막으로 handlerAdapter가 지금은 인터페이스 밖에 지원을 하지 않아서 어노테이션용 adapter도 만들어야한다.</p><pre id="19ca8c72-b114-41ef-9737-58f90728166a" class="code"><code>package org.example.mvc;

import org.example.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof AnnotationHandler;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String viewName = ((AnnotationHandler) handler).handle(request, response);
        return new ModelAndView(viewName);
    }
}</code></pre><p id="b50d36e7-5bb6-4359-8d2c-523743a4af83" class="">
</p></div></article></body></html>
