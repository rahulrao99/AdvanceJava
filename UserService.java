public UserResponse registerAdmin(RegisterRequest request) {
    log.info("Registering new admin with email: {}", request.getEmail());

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new ResourceAlreadyExistsException("Email already registered");
    }

    if (userRepository.existsByUsername(request.getUsername())) {
        throw new ResourceAlreadyExistsException("Username already taken");
    }

    User user = User.builder()
            .id(UUID.randomUUID().toString())
            .username(request.getUsername())
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .phone(request.getPhone())
            .role("ROLE_ADMIN")        // ← only difference from register()
            .isActive(true)
            .build();

    userRepository.save(user);
    log.info("Admin registered successfully with id: {}", user.getId());

    return mapToUserResponse(
            userRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"))
    );
}
