package com.gaga.ragspringai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class RagSpringAiApplication implements CommandLineRunner {

    private final VectorStore vectorStore;

    private final ChatModel chatModel;

    private final ChatMemory chatMemory;

    public RagSpringAiApplication(VectorStore vectorStore, ChatModel chatModel, ChatMemory chatMemory) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
    }

    public static void main(String[] args) {
        SpringApplication.run(RagSpringAiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> contents = List.of(
                "يستخدم Spring Boot تقنية حقن التبعيات (Dependency Injection) لإدارة وحقن التبعيات بين مكونات التطبيق بشكل تلقائي.",
                "مع تقنية DI، يكون Spring مسؤولاً عن إنشاء وإدارة دورة حياة الكائنات التي تحتاجها المكونات الأخرى.",
                "تشير التعليمة @Component إلى أن هذه الفئة مرشحة ليتم التعامل معها كـ Bean تتم إدارتها من قبل حاوية Spring.",
                "تُستخدم @Service للإعلان عن فئة كطبقة خدمة، مما يجعلها أيضاً Bean قابلاً للحقن.",
                "تُستخدم @Repository للمكونات التي تتعامل مع قاعدة البيانات، وتوفر ميزات إضافية مثل ترجمة الاستثناءات.",
                "يقوم Spring Boot تلقائياً بعمل مسح (scan) للحزم الفرعية التي تحتوي على فئات معنونة بـ @Component.",
                "تتيح @Autowired لـ Spring حقن التبعيات تلقائياً داخل الحقول أو المُنشئ (constructor) أو طرق setter.",
                "يُفضل استخدام الحقن عبر المُنشئ على الحقن عبر الحقول لأنه يضمن توفر كل التبعيات عند إنشاء الكائن.",
                "يوفر الحقن عبر setter مرونة أكبر لكنه عرضة للأخطاء إذا لم تُعيَّن التبعيات قبل الاستخدام.",
                "الحقن عبر الحقول سهل الاستخدام لكنه يُصعب عملية الاختبار لأن الحقول تكون خاصة ولا يمكن تغييرها إن كانت نهائية.",
                "تُستخدم @Qualifier مع @Autowired لاختيار Bean معين عند وجود عدة Beans من نفس النوع.",
                "تحدث التبعيات الدائرية عندما تعتمد Beans على بعضها البعض، مما يؤدي إلى خطأ عند إنشاء السياق (Context).",
                "يمكن تعريف Beans أيضاً يدوياً باستخدام التعليمة @Bean داخل فئة تحمل التعليمة @Configuration.",
                "عند استخدام التكوين اليدوي، يجب إعادة كائن Bean من خلال دالة معنونة بـ @Bean.",
                "يقوم Spring باكتشاف الفئات المعنونة من خلال المسح التلقائي (component scanning) انطلاقاً من الحزمة الرئيسية للتطبيق.",
                "تساعد تقنية DI على تقليل الترابط القوي (tight coupling) وجعل المكونات أسهل في الاختبار والصيانة.",
                "يمكن استخدام @Primary لتحديد Bean افتراضي عندما يكون هناك أكثر من Bean من نفس النوع.",
                "من أفضل الممارسات في DI جعل التبعيات غير قابلة للتغيير (final) وحقنها عبر المُنشئ.",
                "مع DI، لا يحتاج التطبيق إلى إنشاء كائنات التبعيات باستخدام الكلمة المفتاحية new.",
                "يمكن لـ Spring Boot تكوين بعض التبعيات تلقائياً إذا كانت مكتبة طرف ثالث متاحة في classpath.",
                "إذا لم يجد Spring Bean للحقن، فسيحدث استثناء من نوع NoSuchBeanDefinitionException عند تشغيل التطبيق.",
                "من مزايا DI تقليل التكرار في الشيفرة (boilerplate) وتحسين قابلية الاختبار من خلال إمكانية استخدام كائنات Mock.",
                "تُستخدم @ComponentScan لتحديد الحزم التي يجب مسحها بحثاً عن Beans تلقائياً.",
                "الـ Beans المعرفة يدوياً باستخدام @Bean تأخذ اسماً افتراضياً مطابقاً لاسم الدالة.",
                "عند استخدام @Autowired مع أكثر من مُنشئ، قد يواجه Spring غموضاً يؤدي إلى فشل في الحقن.",
                "تتيح DI في Spring إنشاء Beans من نوع prototype يتم إنشاؤها من جديد في كل مرة يتم حقنها.",
                "يتم إدارة دورة حياة Bean من نوع singleton بالكامل من قبل Spring، ويتم إنشاؤه مرة واحدة فقط لكل ApplicationContext.",
                "يمكن استخدام @Lazy لتأخير إنشاء Bean حتى يتم طلبه فعلياً.",
                "يمكن استخدام @Scope لتحديد نطاق Bean، مثل singleton أو prototype أو request أو session.",
                "يدعم Spring Boot أيضاً حقن التبعيات في ApplicationRunner و CommandLineRunner لتهيئة التطبيق عند البدء."
        );

        List<Document> documents = IntStream.range(0, contents.size())
                .mapToObj(i -> Document.builder()
                        .text(contents.get(i))
                        .build()
                ).toList();

        vectorStore.add(documents);
    }

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore)
                )
                .build();
    }
}
