play-aauth
==========

AdvancedAuth for Play. Currently based on SecureSocial and ReactiveMongo

play.plugins
-----------

In addition to or as a replacement for securesocial plugins add:

```
9998:mirari.aauth.AuthUserService
9994:mirari.aauth.PersistentAuthenticatorStore
```

+ you may override:

- mirari.aauth.RequestAuthenticator
- mirari.aauth.ProfileService
- mirari.aauth.TokenService

Usage
----------

Use securesocial to implement and plug in auth providers.

Use ProfileService to add your own User Profile class -- optionally connected to a number of different social profiles.

Use `mirari.aauth.AuthPlugins` to access injected/overriden functionality.

Use `mirari.aauth.UserRequiredAction` and `mirari.aauth.UserAwareAction` to bring current user id to scope.

Override `mirari.aauth.RequestAuthenticator` to change request authentication strategy.

Secure your actions using `mirari.aauth.UserAware` and `mirari.aauth.UserRequired` traits.

Use `mirari.aauth.ProfileId` to identify your user inside the system. Create implicit convertions for your UserProfile into ProfileId.

Enjoy.
