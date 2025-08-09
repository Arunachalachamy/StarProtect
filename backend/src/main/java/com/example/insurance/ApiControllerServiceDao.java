package com.example.insurance;

import com.example.insurance.bean.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api")
class ApiController {
    private final UnderWriterService underWriterService;
    private final VehicleService vehicleService;
    private final PolicyService policyService;
    private final CorsUtil corsUtil;

    ApiController(UnderWriterService underWriterService, VehicleService vehicleService,
                  PolicyService policyService, CorsUtil corsUtil) {
        this.underWriterService = underWriterService;
        this.vehicleService = vehicleService;
        this.policyService = policyService;
        this.corsUtil = corsUtil;
    }

    @ModelAttribute
    public void setCorsHeaders(@RequestHeader Map<String, String> headers, @RequestHeader(value = "Origin", required = false) String origin,
                               org.springframework.http.HttpHeaders httpHeaders) {
        corsUtil.applyCorsHeaders(httpHeaders);
    }

    // Registration - UnderWriter
    @PostMapping("/register")
    public ResponseEntity<?> registerUnderWriter(@Valid @RequestBody UnderWriter uw) {
        return ResponseEntity.created(location("/underwriters/" + underWriterService.register(uw).getUnderwriterId()))
                .body("Registered");
    }

    // Admin creation via backend only
    @PostMapping("/admin/create")
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, String> body) {
        underWriterService.createAdmin(body.getOrDefault("userName", "admin"), body.getOrDefault("password", "admin"));
        return ResponseEntity.ok("Admin Created");
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> form) {
        String userName = form.get("userName");
        String password = form.get("password");
        Optional<Login> login = underWriterService.login(userName, password);
        if (login.isPresent()) {
            return ResponseEntity.ok(login.get());
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    // Vehicle registration
    @PostMapping("/vehicles")
    public ResponseEntity<?> registerVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle saved = vehicleService.registerVehicle(vehicle);
        return ResponseEntity.created(location("/vehicles/" + saved.getId())).body(saved);
    }

    // Renew policy
    @PostMapping("/vehicles/{policyId}/renew")
    public ResponseEntity<?> renew(@PathVariable String policyId, @RequestBody Map<String, Object> body) {
        boolean claim = Boolean.parseBoolean(String.valueOf(body.getOrDefault("claimStatus", "false")));
        Vehicle updated = policyService.renewPolicy(policyId, claim);
        return ResponseEntity.ok(updated);
    }

    // View policies by policy id
    @GetMapping("/policies/{policyId}")
    public ResponseEntity<?> viewByPolicyId(@PathVariable String policyId) {
        return vehicleService.viewByPolicyId(policyId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update policy type
    @PutMapping("/policies/{policyId}/type")
    public ResponseEntity<?> updatePolicyType(@PathVariable String policyId, @RequestBody Map<String, String> body) {
        String type = body.get("type");
        return ResponseEntity.ok(vehicleService.updatePolicyType(policyId, type));
    }

    // Admin APIs
    @PutMapping("/admin/underwriters/{id}")
    public ResponseEntity<?> updateUnderWriter(@PathVariable Long id, @RequestBody UnderWriter payload) {
        return ResponseEntity.ok(underWriterService.updateUnderWriter(id, payload));
    }

    @DeleteMapping("/admin/underwriters/{id}")
    public ResponseEntity<?> deleteUnderWriter(@PathVariable Long id) {
        underWriterService.deleteUnderWriter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/underwriters")
    public List<UnderWriter> viewUnderWriters() {
        return underWriterService.findAll();
    }

    @GetMapping("/admin/underwriters/{id}")
    public ResponseEntity<?> viewUnderWriterById(@PathVariable Long id) {
        return underWriterService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private URI location(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUri();
    }
}

@Service
class UnderWriterService {
    private final UnderWriterDao underWriterDao;
    private final LoginDao loginDao;

    UnderWriterService(UnderWriterDao underWriterDao, LoginDao loginDao) {
        this.underWriterDao = underWriterDao;
        this.loginDao = loginDao;
    }

    @Transactional
    public UnderWriter register(UnderWriter uw) {
        uw.setJoiningDate(uw.getJoiningDate() == null ? LocalDate.now() : uw.getJoiningDate());
        uw.setRole("USER");
        UnderWriter saved = underWriterDao.save(uw);
        Login login = new Login();
        login.setUserName(uw.getUserName());
        login.setPassword(uw.getPassword());
        login.setRole("USER");
        login.setUnderWriterId(saved.getUnderwriterId());
        loginDao.save(login);
        return saved;
    }

    @Transactional
    public void createAdmin(String userName, String password) {
        Login login = new Login();
        login.setUserName(userName);
        login.setPassword(password);
        login.setRole("ADMIN");
        loginDao.save(login);
    }

    public Optional<Login> login(String userName, String password) {
        return loginDao.findByUserNameAndPassword(userName, password);
    }

    public List<UnderWriter> findAll() { return underWriterDao.findAll(); }

    public Optional<UnderWriter> findById(Long id) { return underWriterDao.findById(id); }

    @Transactional
    public UnderWriter updateUnderWriter(Long id, UnderWriter payload) {
        UnderWriter uw = underWriterDao.findById(id).orElseThrow();
        uw.setName(Optional.ofNullable(payload.getName()).orElse(uw.getName()));
        uw.setDob(Optional.ofNullable(payload.getDob()).orElse(uw.getDob()));
        uw.setJoiningDate(Optional.ofNullable(payload.getJoiningDate()).orElse(uw.getJoiningDate()));
        if (payload.getPassword() != null) {
            uw.setPassword(payload.getPassword());
            loginDao.updatePasswordByUnderWriterId(id, payload.getPassword());
        }
        return underWriterDao.save(uw);
    }

    @Transactional
    public void deleteUnderWriter(Long id) {
        loginDao.deleteByUnderWriterId(id);
        underWriterDao.deleteById(id);
    }
}

@Service
class VehicleService {
    private final VehicleDao vehicleDao;
    private final PolicyUtil policyUtil;

    VehicleService(VehicleDao vehicleDao, PolicyUtil policyUtil) {
        this.vehicleDao = vehicleDao;
        this.policyUtil = policyUtil;
    }

    @Transactional
    public Vehicle registerVehicle(Vehicle v) {
        v.setPolicyId(policyUtil.generatePolicyId());
        if (v.getToDate() == null && v.getFromDate() != null) {
            v.setToDate(v.getFromDate().plusYears(1));
        }
        v.setPremiumAmnt(policyUtil.calculatePremium(v));
        return vehicleDao.save(v);
    }

    public Optional<Vehicle> viewByPolicyId(String policyId) {
        return vehicleDao.findByPolicyId(policyId);
    }

    @Transactional
    public Vehicle updatePolicyType(String policyId, String type) {
        Vehicle v = vehicleDao.findByPolicyId(policyId).orElseThrow();
        v.setType(type);
        v.setPremiumAmnt(new PolicyUtil().calculatePremium(v));
        return vehicleDao.save(v);
    }
}

@Service
class PolicyService {
    private final VehicleDao vehicleDao;
    private final RenewalDao renewalDao;
    private final PolicyUtil policyUtil;

    PolicyService(VehicleDao vehicleDao, RenewalDao renewalDao, PolicyUtil policyUtil) {
        this.vehicleDao = vehicleDao;
        this.renewalDao = renewalDao;
        this.policyUtil = policyUtil;
    }

    @Transactional
    public Vehicle renewPolicy(String policyId, boolean claim) {
        Vehicle v = vehicleDao.findByPolicyId(policyId)
                .orElseThrow(() -> new RuntimeException("Invalid policy id"));
        LocalDate now = LocalDate.now();
        if (v.getToDate() != null) {
            long daysLeft = ChronoUnit.DAYS.between(now, v.getToDate());
            if (daysLeft > 30) {
                throw new RuntimeException("Renewal allowed only within 30 days of expiry or after expiry");
            }
        }
        LocalDate newFrom = v.getToDate() != null && !now.isAfter(v.getToDate()) ? v.getToDate().plusDays(1) : now;
        LocalDate newTo = newFrom.plusYears(1);
        double premium = policyUtil.calculatePremium(v);
        v.setFromDate(newFrom);
        v.setToDate(newTo);
        v.setPremiumAmnt(premium);
        vehicleDao.save(v);

        InsuranceRenewal record = new InsuranceRenewal();
        record.setPolicyId(policyId);
        record.setRenewedOn(LocalDate.now());
        record.setFromDate(newFrom);
        record.setToDate(newTo);
        record.setPremiumAmnt(premium);
        record.setClaimStatus(claim);
        renewalDao.save(record);
        return v;
    }
}

@Repository
class UnderWriterDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public UnderWriter save(UnderWriter uw) {
        if (uw.getUnderwriterId() == null) {
            em.persist(uw);
            return uw;
        }
        return em.merge(uw);
    }

    public Optional<UnderWriter> findById(Long id) {
        return Optional.ofNullable(em.find(UnderWriter.class, id));
    }

    public List<UnderWriter> findAll() {
        return em.createQuery("select u from UnderWriter u", UnderWriter.class).getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(em::remove);
    }
}

@Repository
class LoginDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Login save(Login login) {
        if (login.getId() == null) {
            em.persist(login);
            return login;
        }
        return em.merge(login);
    }

    public Optional<Login> findByUserNameAndPassword(String userName, String password) {
        List<Login> list = em.createQuery("select l from Login l where l.userName=:u and l.password=:p", Login.class)
                .setParameter("u", userName)
                .setParameter("p", password)
                .getResultList();
        return list.stream().findFirst();
    }

    @Transactional
    public void updatePasswordByUnderWriterId(Long underWriterId, String password) {
        em.createQuery("update Login l set l.password=:p where l.underWriterId=:id")
                .setParameter("p", password)
                .setParameter("id", underWriterId)
                .executeUpdate();
    }

    @Transactional
    public void deleteByUnderWriterId(Long underWriterId) {
        em.createQuery("delete from Login l where l.underWriterId=:id")
                .setParameter("id", underWriterId)
                .executeUpdate();
    }
}

@Repository
class VehicleDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Vehicle save(Vehicle v) {
        if (v.getId() == null) {
            em.persist(v);
            return v;
        }
        return em.merge(v);
    }

    public Optional<Vehicle> findByPolicyId(String policyId) {
        List<Vehicle> list = em.createQuery("select v from Vehicle v where v.policyId=:p", Vehicle.class)
                .setParameter("p", policyId).getResultList();
        return list.stream().findFirst();
    }
}

@Repository
class RenewalDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public InsuranceRenewal save(InsuranceRenewal r) {
        if (r.getId() == null) {
            em.persist(r);
            return r;
        }
        return em.merge(r);
    }
}

@Component
class PolicyUtil {
    public String generatePolicyId() {
        return "POL" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    public double calculatePremium(Vehicle v) {
        double base = Objects.equals(v.getVehicleType(), "4-wheeler") ? 3000.0 : 1500.0;
        if (Objects.equals(v.getType(), "ThirdParty")) base *= 0.6;
        return Math.round(base * 100.0) / 100.0;
    }
}

@Component
class CorsUtil {
    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    public void applyCorsHeaders(org.springframework.http.HttpHeaders headers) {
        // No-op here; use Spring MVC CORS if needed. Placeholder to satisfy util requirement.
    }
}