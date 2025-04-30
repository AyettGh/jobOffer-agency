package org.example.recrutement.Controller;

import org.example.recrutement.DTO.RegisterRequest;
import org.example.recrutement.DTO.UserRegisteredEvent;
import org.example.recrutement.Entity.*;
import org.example.recrutement.Entity.Enum.RoleEnum;
import org.example.recrutement.Entity.Enum.StatusApplicationEnum;
import org.example.recrutement.RabbitMQ.EmailService;
import org.example.recrutement.Service.ServiceImpl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/")
public class ThymeleafController {

    @Autowired private AgencyService agencyService;
    @Autowired private ApplicationService applicationService;
    @Autowired private CategoryService categoryService;
    @Autowired private ClientService clientService;
    @Autowired private OfferService offerService;
    @Autowired private UserService userService;
    @Autowired private EmailService emailService;



    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "success", required = false) String success,
            Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password!");
        if (logout != null) model.addAttribute("message", "You have been logged out successfully.");
        if (success != null) model.addAttribute("success", success);
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("roles", RoleEnum.values());
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(
            @ModelAttribute RegisterRequest registerRequest,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        try {
            if (!registerRequest.getPassword().equals(confirmPassword)) {
                throw new RuntimeException("Passwords do not match");
            }

            User user = userService.registerUser(registerRequest);


            System.out.println("User email: " + user.getEmail());
            UserRegisteredEvent event = new UserRegisteredEvent(
                    user.getEmail(),
                    user.getUsername()
            );
            emailService.sendUserRegistrationEmail(event);

            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/redirect-by-role")
    public String redirectByRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .map(authority -> {
                    switch (authority) {
                        case "ADMIN": return "redirect:/admin/dashboard";
                        case "AGENCY": return "redirect:/agency/new";
                        case "CLIENT": return "redirect:/client";
                        default: return "redirect:/login?error";
                    }
                })
                .orElse("redirect:/login?error");
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }


    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("agencyCount", agencyService.count());
        model.addAttribute("offerCount", offerService.count());
        model.addAttribute("applicationCount", applicationService.count());
        return "admin/dashboard";
    }


    @GetMapping("/agencies")
    public String listAgencies(Model model) {
        model.addAttribute("agencies", agencyService.all());
        return "agency-list";
    }

    @GetMapping("/agencies/new")
    public String createAgencyForm(Model model) {
        model.addAttribute("agency", new Agency());
        return "agency-form";
    }

    @PostMapping("/agencies/new")
    public String saveAgency(@ModelAttribute("agency") Agency agency,
                             RedirectAttributes redirectAttributes) {
        try {
            agencyService.add(agency);
            redirectAttributes.addFlashAttribute("success", "Agency created successfully!");
            return "redirect:/agencies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating agency: " + e.getMessage());
            return "redirect:/agencies/new";
        }
    }

    @GetMapping("/agencies/delete/{id}")
    public String deleteAgencies(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agencyService.delete(id);
            redirectAttributes.addFlashAttribute("success", "agency deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting offer: " + e.getMessage());
        }
        return "redirect:/agencies";
    }

    @GetMapping("/agencies/edit/{id}")
    public String editAgenciesForm(@PathVariable Long id, Model model) {
        model.addAttribute("agency", agencyService.getById(id));
        model.addAttribute("offers", offerService.all());
        model.addAttribute("users", userService.all());
        return "agency-form";
    }

    @PostMapping("/agencies/edit/{id}")
    public String updateAgency(@PathVariable Long id,
                               @ModelAttribute("agency") Agency agency,
                               RedirectAttributes redirectAttributes) throws IOException {
        try {
            Agency existingAgency = agencyService.getById(id);
            agency.setId(id);

            agencyService.add(agency);

            redirectAttributes.addFlashAttribute("success", "Agency updated successfully!");
            return "redirect:/agencies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating agency: " + e.getMessage());
            return "redirect:/agencies/edit/" + id;
        }
    }


    @GetMapping("/applications")
    public String listApplications(Model model) {
        model.addAttribute("applications", applicationService.all());
        return "application-list";
    }

    @GetMapping("/applications/new")
    public String createApplicationForm(Model model) {
        List<Offer> offers = offerService.all();
        List<Client> clients = clientService.all();

        System.out.println("Offers: " + offers.size());
        System.out.println("Clients: " + clients.size());

        model.addAttribute("application", new Application());
        model.addAttribute("offers", offers);
        model.addAttribute("clients", clients);
        model.addAttribute("application", new Application());
        model.addAttribute("offers", offerService.all());
        model.addAttribute("clients", clientService.all());
        return "application-form";
    }

    @PostMapping("/applications/new")
    public String saveApplication(@ModelAttribute("application") Application application,
                                  @RequestParam("cvFile") MultipartFile cvFile,
                                  @RequestParam("offerId") Long offerId,
                                  @RequestParam("clientId") Long clientId,
                                  RedirectAttributes redirectAttributes) {


        try {
            if (cvFile.isEmpty()) {
                throw new IllegalArgumentException("CV file is required");
            }

            if (!"application/pdf".equalsIgnoreCase(cvFile.getContentType())) {
                throw new IllegalArgumentException("Only PDF files are allowed");
            }

            Offer offer = offerService.getById(offerId);
            Client client = clientService.getById(clientId);

            application.setCvPdf(cvFile.getBytes());
            application.setCvFileName(cvFile.getOriginalFilename());
            application.setOffer(offer);
            application.setClient(client);
            application.setSubmittedAt(LocalDateTime.now());

            Application savedApplication = applicationService.add(application);

            Context clientContext = new Context();
            clientContext.setVariable("name", client.getUser().getUsername());
            clientContext.setVariable("offerTitle", offer.getTitle());
            clientContext.setVariable("applicationId", savedApplication.getId());
            System.out.println("Client email: " + client.getUser().getEmail());
            System.out.println("Offer title: " + offer.getTitle());
            System.out.println("Application ID: " + savedApplication.getId());
            emailService.sendHtmlEmail(
                    client.getUser().getEmail(),
                    "Application Submission Confirmation",
                    "email/application-submission-client",
                    clientContext
            );

            Context adminContext = new Context();
            adminContext.setVariable("clientName", client.getUser().getUsername());
            adminContext.setVariable("offerTitle", offer.getTitle());
            adminContext.setVariable("applicationId", savedApplication.getId());

            emailService.sendHtmlEmail(
                    "admin@recruitment.com",
                    "New Application Received",
                    "email/application-submission-admin",
                    adminContext
            );

            redirectAttributes.addFlashAttribute("success", "Application submitted successfully! Confirmation sent to your email.");
            return "redirect:/applications";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting application: " + e.getMessage());
            return "redirect:/applications/new";
        }
    }
    @PostMapping("/applications/status/update")
    public String updateStatus(@RequestParam Long applicationId,
                               @RequestParam StatusApplicationEnum status,
                               RedirectAttributes redirectAttributes) {
        try {
            Application application = applicationService.getById(applicationId);
            if (application != null) {
                application.setStatus(status);
                application.setUpdatedAt(LocalDateTime.now());
                applicationService.add(application);
                redirectAttributes.addFlashAttribute("success", "Status updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Application not found!");
            }
            return "redirect:/applications";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating status: " + e.getMessage());
            return "redirect:/applications";
        }
    }
    @GetMapping("/applications/delete/{id}")
    public String deleteApplications(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            applicationService.delete(id);
            redirectAttributes.addFlashAttribute("success", "application deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting offer: " + e.getMessage());
        }
        return "redirect:/applications";
    }

    @GetMapping("/offers")
    public String listOffers(Model model) {
        model.addAttribute("offers", offerService.all());
        return "offer_list";
    }

    @GetMapping("/offers/new")
    public String createOfferForm(Model model) {
        model.addAttribute("offer", new Offer());
        model.addAttribute("agencies", agencyService.all());
        model.addAttribute("categories", categoryService.all());
        return "offer_form";
    }

    @PostMapping("/offers/new")
    public String saveOffer(@ModelAttribute("offer") Offer offer,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            RedirectAttributes redirectAttributes) throws IOException {
        try {
            if (!imageFile.isEmpty()) {
                offer.setImage(imageFile.getBytes());
            }

            offer.setCreatedAt(LocalDateTime.now());
            offerService.add(offer);
            redirectAttributes.addFlashAttribute("success", "Offer created successfully!");
            return "redirect:/offers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating offer: " + e.getMessage());
            return "redirect:/offers/new";
        }
    }

    @GetMapping("/offers/edit/{id}")
    public String editOfferForm(@PathVariable Long id, Model model) {
        model.addAttribute("offer", offerService.getById(id));
        model.addAttribute("agencies", agencyService.all());
        model.addAttribute("categories", categoryService.all());
        return "offer_form";
    }

    @PostMapping("/offers/edit/{id}")
    public String updateOffer(@PathVariable Long id,
                              @ModelAttribute("offer") Offer offer,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) throws IOException {
        try {
            Offer existingOffer = offerService.getById(id);
            offer.setId(id);

            if (imageFile.isEmpty()) {
                offer.setImage(existingOffer.getImage());
            } else {
                offer.setImage(imageFile.getBytes());
            }

            offerService.add(offer);
            redirectAttributes.addFlashAttribute("success", "Offer updated successfully!");
            return "redirect:/offers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating offer: " + e.getMessage());
            return "redirect:/offers/edit/" + id;
        }
    }

    @GetMapping("/offers/delete/{id}")
    public String deleteOffer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            offerService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Offer deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting offer: " + e.getMessage());
        }
        return "redirect:/offers";
    }

    @GetMapping("/offers/image/{id}")
    public String viewOfferImage(@PathVariable Long id, Model model) {
        Offer offer = offerService.getById(id);
        if (offer.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(offer.getImage());
            model.addAttribute("image", base64Image);
            return "image-view";
        }
        return "redirect:/offers";
    }


    @GetMapping("/client")
    public String viewOffersForClient(Model model) {
        model.addAttribute("offers", offerService.all());
        return "client/offer_list";
    }

    @GetMapping("/client/new")
    public String createApplicationForma(Model model) {
        List<Offer> offers = offerService.all();
        List<Client> clients = clientService.all();

        model.addAttribute("application", new Application());
        model.addAttribute("offers", offers);
        model.addAttribute("clients", clients);

        return "client/clientapplication-form";
    }


    @PostMapping("/client/new")
    public String saveClientApplication(@ModelAttribute("application") Application application,
                                        @RequestParam("cvFile") MultipartFile cvFile,
                                        @RequestParam("offerId") Long offerId,
                                        @RequestParam("clientId") Long clientId,
                                        RedirectAttributes redirectAttributes) {
        try {
            if (cvFile.isEmpty()) {
                throw new IllegalArgumentException("CV file is required");
            }

            if (!"application/pdf".equalsIgnoreCase(cvFile.getContentType())) {
                throw new IllegalArgumentException("Only PDF files are allowed");
            }

            Offer offer = offerService.getById(offerId);
            Client client = clientService.getById(clientId);

            application.setCvPdf(cvFile.getBytes());
            application.setCvFileName(cvFile.getOriginalFilename());
            application.setOffer(offer);
            application.setClient(client);
            application.setSubmittedAt(LocalDateTime.now());

            Application savedApplication = applicationService.add(application);

            Context clientContext = new Context();
            clientContext.setVariable("name", client.getUser().getUsername());
            clientContext.setVariable("offerTitle", offer.getTitle());
            clientContext.setVariable("applicationId", savedApplication.getId());
            System.out.println("Client email: " + client.getUser().getEmail());
            System.out.println("Offer title: " + offer.getTitle());
            System.out.println("Application ID: " + savedApplication.getId());
            emailService.sendHtmlEmail(
                    client.getUser().getEmail(),
                    "Application Submission Confirmation",
                    "email/application-submission-client",
                    clientContext
            );

            Context adminContext = new Context();
            adminContext.setVariable("clientName", client.getUser().getUsername());
            adminContext.setVariable("offerTitle", offer.getTitle());
            adminContext.setVariable("applicationId", savedApplication.getId());

            emailService.sendHtmlEmail(
                    "admin@recruitment.com",
                    "New Application Received",
                    "email/application-submission-admin",
                    adminContext
            );

            redirectAttributes.addFlashAttribute("success", "Application submitted successfully!");
            return "redirect:/client";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting application: " + e.getMessage());
            return "redirect:/client/new";
        }
    }

    @GetMapping("/agency/new")
    public String createOfferForma(Model model) {
        model.addAttribute("offer", new Offer());
        model.addAttribute("agencies", agencyService.all());
        model.addAttribute("categories", categoryService.all());
        return "agency/offer_form";
    }

    @PostMapping("/agency/new")
    public String saveOffera(@ModelAttribute("offer") Offer offer,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            RedirectAttributes redirectAttributes) throws IOException {
        try {
            if (!imageFile.isEmpty()) {
                offer.setImage(imageFile.getBytes());
            }

            offer.setCreatedAt(LocalDateTime.now());
            offerService.add(offer);
            redirectAttributes.addFlashAttribute("success", "Offer created successfully!");
            return "redirect:/agency/new";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating offer: " + e.getMessage());
            return "redirect:/agency/new";
        }
    }
    @GetMapping("/agency/image/{id}")
    public String viewOfferImagea(@PathVariable Long id, Model model) {
        Offer offer = offerService.getById(id);
        if (offer.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(offer.getImage());
            model.addAttribute("image", base64Image);
            return "image-view";
        }
        return "redirect:/agency/new";
    }

}
