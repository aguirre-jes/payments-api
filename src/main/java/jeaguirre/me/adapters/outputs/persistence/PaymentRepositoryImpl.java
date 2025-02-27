package jeaguirre.me.adapters.outputs.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jeaguirre.me.adapters.outputs.persistence.entities.Payment;
import jeaguirre.me.domains.ports.PaymentRepository;

@ApplicationScoped
public class PaymentRepositoryImpl implements PaymentRepository {

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Payment payment) {
        entityManager.persist(payment);
    }

    @Override
    public Optional<Payment> findById(int id) {
        Payment payment = entityManager.find(Payment.class, id);
        return payment != null ? Optional.of(payment) : Optional.empty();
    }

    @Override
    @Transactional
    public void update(Payment payment) {
        entityManager.merge(payment);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Payment payment = entityManager.find(Payment.class, id);
        if (payment != null) {
            entityManager.remove(payment);
        }
    }

    @Override
    public List<Payment> findByStatus(String status) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.status = :status", Payment.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Payment> findByPayerId(int payerId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.payerId = :payerId", Payment.class);
        query.setParameter("payerId", payerId);
        return query.getResultList();
    }

    @Override
    public List<Payment> findByPayeeId(int payeeId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.payeeId = :payeeId", Payment.class);
        query.setParameter("payeeId", payeeId);
        return query.getResultList();
    }

    @Override
    public List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate", Payment.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

}
