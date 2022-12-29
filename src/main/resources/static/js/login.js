const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            data: [],
            clients: [],
            signInEmail: "",
            signInPassword: "",
            logout: new URLSearchParams(location.search).get('logout'),
            error: new URLSearchParams(location.search).get('error'),
            signUpFirstName: "",
            signUpLastName: "",
            signUpEmail: "",
            signUpPassword: "",
        }
    },
    created() {
        console.log(this.logout);
        console.log(this.error);
        if(this.logout != null){
            Toastify({
                text: "Logout has been successfully",
                duration: 5000,
                close: true,
                gravity: "top",
                position: "center",
                stopOnFocus: true,
                style: {
                  background: "linear-gradient(to right, #64d11b, #23c072)",
                  "border-radius": "10px"
                },
            }).showToast();
        }
        if(this.error != null){
            Toastify({
                text: "Check the data and retry",
                duration: 5000,
                close: true,
                gravity: "top",
                position: "center",
                stopOnFocus: true,
                style: {
                  background: "linear-gradient(to right, #d11b1b, #ec6464)",
                  "border-radius": "10px"
                },
            }).showToast();
        }
    },
    mounted() {
        const signUpButton = document.getElementById("signUp");
        const signInButton = document.getElementById("signIn");
        const container = document.getElementById("container");

        signUpButton.addEventListener("click", () => {
            container.classList.add("right-panel-active");
        });

        signInButton.addEventListener("click", () => {
            container.classList.remove("right-panel-active");
        });

    },
    methods: {
        signIn(){
            axios.post('/api/login',
            "email=" + this.signInEmail +
            "&password=" + this.signInPassword)
            .then(response => {
                console.log(response);
                location.href=response.request.responseURL
            })
            .catch(error => console.log(error))
        },
        signUp(){
            axios.post('/api/clients',
            "firstName=" + this.signUpFirstName + 
            "&lastName=" + this.signUpLastName +
            "&email=" + this.signUpEmail +
            "&password=" + this.signUpPassword)
            .then(response =>  {
                console.log(response.data);
                Toastify({
                    text: "Account created successfully",
                    duration: 5000,
                    close: true,
                    gravity: "top",
                    position: "center",
                    stopOnFocus: true,
                    style: {
                      background: "linear-gradient(to right, #64d11b, #23c072)",
                      "border-radius": "10px"
                    },
                }).showToast();
                axios.post('/api/login',"email=" + this.signUpEmail + "&password=" + this.signUpPassword)
                .then(response => {
                    window.location.href="/web/accounts.html"
                })
                .catch(error => console.log(error))
            })
            .catch(error => console.log(error.response.data))
        },
    },
    computed: {

    }
}).mount('#app')