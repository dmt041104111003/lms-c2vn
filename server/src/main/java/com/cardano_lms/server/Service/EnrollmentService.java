package com.cardano_lms.server.Service;

import com.cardano_lms.server.Constant.OrderStatus;
import com.cardano_lms.server.Entity.Course;
import com.cardano_lms.server.Entity.CoursePaymentMethod;
import com.cardano_lms.server.Entity.Enrollment;
import com.cardano_lms.server.Entity.User;
import com.cardano_lms.server.Exception.AppException;
import com.cardano_lms.server.Exception.ErrorCode;
import com.cardano_lms.server.Repository.CoursePaymentMethodRepository;
import com.cardano_lms.server.Repository.CourseRepository;
import com.cardano_lms.server.Repository.EnrollmentRepository;
import com.cardano_lms.server.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CoursePaymentMethodRepository coursePaymentMethodRepository;

    @Value("${BLOCKFROST_PROJECT_ID}")
    private String blockfrostProjectId;

    @Value("${BLOCKFROST_API}")
    private String blockfrostApi;
    public boolean verifyPayment(String expectedReceiver, String expectedSender,
                                 double expectedAmountAda, String txHash) {

        String url = blockfrostApi + "/txs/" + txHash + "/utxos";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("project_id", blockfrostProjectId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.getBody());

            JsonNode inputs = json.get("inputs");
            if (inputs == null || inputs.isEmpty()) return false;
            String senderAddress = inputs.get(0).get("address").asText();

            long expectedLovelace = (long) (expectedAmountAda * 1_000_000);

            JsonNode outputs = json.get("outputs");
            if (outputs == null || outputs.isEmpty()) return false;

            for (JsonNode output : outputs) {
                String receiverAddress = output.get("address").asText();
                for (JsonNode amount : output.get("amount")) {
                    if ("lovelace".equals(amount.get("unit").asText())) {
                        long value = amount.get("quantity").asLong();
                        if (receiverAddress.trim().equals(expectedReceiver.trim())
                                && value >= expectedLovelace) {
                            System.out.println("Payment verified!");
                            return true;
                        }
                    }
                }
            }

            System.out.println("Payment not verified.");
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @Transactional
    public Enrollment createEnrollmentAfterPayment(String userId, String courseId,
                                                   Long coursePaymentMethodId, double priceAda, String txHash) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new AppException(ErrorCode.COURSE_NOT_FOUND)
        );

        if(enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)){
            throw new AppException(ErrorCode.ALREADY_JOIN_THIS_COURSE);
        }

        CoursePaymentMethod method = coursePaymentMethodRepository.findById(coursePaymentMethodId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_HAVE_METHOD));


        String receiverWallet = coursePaymentMethodRepository.findById(coursePaymentMethodId).get().getReceiverAddress();

        boolean validTx = verifyPayment(receiverWallet, user.getWalletAddress(), priceAda, txHash);
        if (!validTx) {
            throw new AppException(ErrorCode.CARDANO_TRANSACTION_NOT_VALID);
        }

        Enrollment enrollment = Enrollment.builder()
                .enrolledAt(LocalDateTime.now())
                .completed(false)
                .price(priceAda)
                .coursePaymentMethod(method)
                .orderId(UUID.randomUUID().toString())
                .status(OrderStatus.SUCCESS)
                .course(course)
                .user(user)
                .build();
        return enrollmentRepository.save(enrollment);
    }
}
