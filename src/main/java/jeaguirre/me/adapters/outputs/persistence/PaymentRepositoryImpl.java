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
import jeaguirre.me.domains.exceptions.PaymentSaveException;
import jeaguirre.me.domains.exceptions.PaymentSearchException;
import jeaguirre.me.domains.exceptions.PaymentUpdateException;
import jeaguirre.me.domains.ports.PaymentRepository;

@ApplicationScoped
public class PaymentRepositoryImpl implements PaymentRepository {

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Payment payment) {
        try {
            entityManager.persist(payment);
        } catch (Exception e) {
            throw new PaymentSaveException("Error saving payment with ID: " + payment.getId(), e);
        }
    }

    @Override
    public Optional<Payment> findById(int id) {
        try {
            Payment payment = entityManager.find(Payment.class, id);
            if (payment == null) {
                throw new PaymentSearchException("Payment with ID: " + id + " not found", null);
            }
            return Optional.of(payment);
        } catch (Exception e) {
            throw new PaymentSearchException("Error finding payment with ID: " + id, e);
        }
    }

    @Override
    @Transactional
    public void update(Payment payment) {
        if (payment == null) {
            throw new PaymentUpdateException("Error updating payment object is null", null);
        }
        try {
            entityManager.merge(payment);
        } catch (Exception e) {
            throw new PaymentUpdateException("Error updating payment with ID: " + payment.getId(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        try {
            Payment payment = entityManager.find(Payment.class, id);
            if (payment == null) {
                throw new PaymentSearchException("Payment with ID: " + id + " not found", null);
            }
            entityManager.remove(payment);
        } catch (Exception e) {
            throw new PaymentSearchException("Error deleting payment with ID: " + id, e);
        }
    }

    @Override
    public List<Payment> findByStatus(String status) {
        try {
            TypedQuery<Payment> query = entityManager.createQuery(
                    "SELECT p FROM Payment p WHERE p.status = :status", Payment.class);
            query.setParameter("status", status);
            List<Payment> results = query.getResultList();
            if (results.isEmpty()) {
                throw new PaymentSearchException("No payments found with status: " + status, null);
            }
            return results;
        } catch (Exception e) {
            throw new PaymentSearchException("Error finding payments with status: " + status, e);
        }
    }

    @Override
    public List<Payment> findByPayerId(int payerId) {
        try {
            TypedQuery<Payment> query = entityManager.createQuery(
                    "SELECT p FROM Payment p WHERE p.payerId = :payerId", Payment.class);
            query.setParameter("payerId", payerId);
            List<Payment> results = query.getResultList();
            if (results.isEmpty()) {
                throw new PaymentSearchException("No payments found with payer ID: " + payerId, null);
            }
            return results;
        } catch (Exception e) {
            throw new PaymentSearchException("Error finding payments with payer ID: " + payerId, e);
        }
    }

    @Override
    public List<Payment> findByPayeeId(int payeeId) {
        try {
            TypedQuery<Payment> query = entityManager.createQuery(
                    "SELECT p FROM Payment p WHERE p.payeeId = :payeeId", Payment.class);
            query.setParameter("payeeId", payeeId);
            List<Payment> results = query.getResultList();
            if (results.isEmpty()) {
                throw new PaymentSearchException("No payments found with payee ID: " + payeeId, null);
            }
            return results;
        } catch (Exception e) {
            throw new PaymentSearchException("Error finding payments with payee ID: " + payeeId, e);
        }
    }

    @Override
    public List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            TypedQuery<Payment> query = entityManager.createQuery(
                    "SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate", Payment.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            List<Payment> results = query.getResultList();
            if (results.isEmpty()) {
                throw new PaymentSearchException("No payments found between dates: " + startDate + " and " + endDate,
                        null);
            }
            return results;
        } catch (Exception e) {
            throw new PaymentSearchException("Error finding payments between dates: " + startDate + " and " + endDate,
                    e);
        }
    }

}
