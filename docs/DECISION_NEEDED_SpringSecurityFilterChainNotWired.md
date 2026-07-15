# Decision Needed: Spring Security's Filter Chain Is Never Wired Into the Servlet Pipeline (Application-Wide)

**Prepared:** 2026-07-15, discovered while investigating `docs/DECISION_NEEDED_RazorPayClientTrustedAmount.md` §4 at owner's explicit request to check for a layer in front of `JwtTokenFilter`. **This finding is not specific to Razorpay** — it affects every URL in the application. Logged as its own item per owner instruction, separate from the Razorpay-specific document.

## The finding

`legacy/HIS-WAR/src/main/java/com/param/config/SecurityConfig.java` is a real, `@EnableWebSecurity`-annotated Spring Security configuration:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/resources/assets/js/**","/adt/organizationMasterList/**","/adt/getUnitByCredentials","/ldapUserCredentialCheck").permitAll()
                .antMatchers(HttpMethod.POST, "/adt/userLogin").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
```

Read on its own, this looks like the application's real access-control policy: everything requires authentication except 5 explicit exceptions, with `JwtTokenFilter` doing the actual token validation. **It has no effect on any request the application actually serves.**

Spring Security only intercepts servlet requests if something bridges its internally-built filter chain into the servlet container's request pipeline — for an XML-configured `web.xml` app like this one (no `WebApplicationInitializer` class exists here), that bridge is a `DelegatingFilterProxy` filter (conventionally named `springSecurityFilterChain`) registered in `web.xml`. A full read of `legacy/HIS-WAR/src/main/webapp/WEB-INF/web.xml` found:

```xml
<filter>
    <filter-name>rateLimitFilter</filter-name>
    <filter-class>com.param.filter.RateLimitFilter</filter-class>
</filter>
<filter-mapping><filter-name>rateLimitFilter</filter-name><url-pattern>/*</url-pattern></filter-mapping>

<filter>
    <filter-name>requestWrap</filter-name>
    <filter-class>com.param.filter.JwtTokenFilter</filter-class>
</filter>
<filter-mapping><filter-name>requestWrap</filter-name><url-pattern>/*</url-pattern></filter-mapping>

<filter>
    <filter-name>cors</filter-name>
    <filter-class>com.param.filter.CORSFilter</filter-class>
</filter>
<filter-mapping><filter-name>cors</filter-name><url-pattern>/*</url-pattern></filter-mapping>
```

Three filters, all mapped to `/*`. **No `DelegatingFilterProxy`, no `springSecurityFilterChain`, anywhere in this file.** Also confirmed: no servlet-spec `<security-constraint>`/`<auth-constraint>`/`<login-config>` (the non-Spring alternative), and no `WebApplicationInitializer` class anywhere in `legacy/` providing some other wiring path. `JwtTokenFilter` here (`requestWrap`) is registered as a **plain servlet filter**, instantiated directly by Tomcat — not through Spring Security's proxy, not through Spring's dependency injection.

**Consequence:** `SecurityConfig.java`'s policy — the `.anyRequest().authenticated()` rule, its 5 `permitAll()` exceptions, even its own separate `new JwtTokenFilter()` instance created inside `addFilterBefore(...)` — are Spring beans that exist in the application context and are **never invoked for a single incoming HTTP request.** The entire application's access control, for every one of its ~7,000 endpoints (per `docs/AUDIT_WholeCodebase_patterns.md`'s earlier count), rests solely on `JwtTokenFilter`'s own hand-rolled logic (the raw `requestWrap` registration) — a single 600-line file with two ~150-entry, largely-duplicated `String.contains()` exemption lists and a blanket `splitUrl.length <= 2` fallback for any short, unlisted URL.

I do not know how long this has been the case, or whether `SecurityConfig.java` was ever actually wired in the past and the bridge was later removed, or was always dead on arrival. `git log` on this specific project only goes back to today's baseline commit (this repository had no version control before this session), so that history isn't available to check.

## Why this is a real decision, not just a bug to silently fix

Wiring `SecurityConfig.java` in as-written would be a large, high-blast-radius behavioral change, not a safe drop-in fix: its `permitAll()` list has only 5 entries, while `JwtTokenFilter`'s own exemption lists (extracted mechanically, deduplicated) contain **201 distinct URL substrings** currently treated as not requiring authentication in production — including patient self-registration, appointment booking, OTP verification, forgot-password, doctor/specialty search, and many more. Applying `.anyRequest().authenticated()` for real, without first reconciling that gap, would immediately break every one of those 201 currently-functioning public flows.

See `docs/REVIEW_DRAFT_razorpay-auth-fix-NOT-APPLIED.md` for a drafted fix — presented for review, **not applied**. It includes both a low-risk, narrowly-scoped patch (close the specific gap in `JwtTokenFilter` itself for the two Razorpay endpoints) and the larger, higher-risk structural fix (wire in `DelegatingFilterProxy` with a reconciled `permitAll()` list), with an explicit recommendation on sequencing given this is live production.

## What we need from you

1. Was `SecurityConfig.java` ever actually active in this application, to your knowledge? Understanding whether this is a regression (something broke a working bridge) versus dead-on-arrival config would help scope how urgently to treat it and whether anything else regressed alongside it.
2. Given the blast radius, do you want the full structural fix (wiring `DelegatingFilterProxy` + reconciling the 201-entry exemption list) treated as its own dedicated, carefully-staged effort — separate from and after the narrower Razorpay-specific fix — rather than bundled together? My recommendation is yes; see the review doc for the full reasoning.
3. This is independent of, but compounds, the Razorpay findings in the sibling document — worth reading both together when you get to prioritization.

Classification: Neither (investigation only, no code/schema/config change this session). No `docs/BUSINESS_RULE_MANIFEST.md` entry added yet, consistent with this project's convention that `DECISION_NEEDED_*` documents are not themselves manifest rows until a fix is decided and implemented. No code, schema, or configuration has been touched to produce this document.
