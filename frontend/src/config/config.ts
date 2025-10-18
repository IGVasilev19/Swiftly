export const config = {
  app: {
    name: "Swiftly",
  },
  user: {
    allowedUpdates: [
      "name",
      "email",
      "password",
      "emailVerified",
      "phoneNumber",
      "address",
      "country",
      "city",
      "postalCode",
      "role",
    ],
    passwordRules: {
      minLength: 8,
      requireUppercase: true,
      requireLowercase: true,
      requireNumber: true,
      requireSymbol: true,
    },
    providers: {
      google: true,
      github: false,
      facebook: true,
      twitter: false,
      linkedin: false,
      instagram: false,
    },
  },
};
