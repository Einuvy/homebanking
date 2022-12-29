const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            id: new URLSearchParams(location.search).get('id'),
            data: [],
            client: {},
            cards:[],
            credit:[],
            debit:[],
            transactionButtonClass: ["button-section", "d-flex", "align-items-center"],
        }
    },
    created() {
        this.getData();
    },
    mounted() {
        const body = document.querySelector("body"),
            sidebar = body.querySelector("nav"),
            toggle = body.querySelector(".toggle"),
            modeSwitch = body.querySelector(".toggle-switch"),
            modeText = body.querySelector(".mode-text"),
            mainContent = body.querySelector(".main-accounts")
            
        toggle.addEventListener("click", () => {
            sidebar.classList.toggle("close");
        });
        modeSwitch.addEventListener("click", () => {
            body.classList.toggle("dark");
            mainContent.classList.toggle("dark")

            if (body.classList.contains("dark")) {
                modeText.innerText = "Light mode";
            } else {
                modeText.innerText = "Dark mode";
            }
        });

    },
    methods: {
        getData() {
            axios.get(`/api/clients/current`)
                .then(response => {
                    this.data = response.data
                    return this.data
                })
                .then(data => {
                    this.client = data;
                    this.cards = this.client.cards;
                    this.credit = this.cards.filter(card => card.type == "Credit")
                    this.debit = this.cards.filter(card => card.type == "Debit")
                    console.log(this.credit);
                    console.log(this.debit);
                })
                .catch(error => console.error(error))
        },
        backOut(){
            this.transactionButtonClass.push("animate__animated");
            this.transactionButtonClass.push("animate__backOutRight");
        },
        logOut(){
            axios.post("/api/logout")
            .then((response) =>{
                console.log("signed out!!");
                window.location.href=response.request.responseURL
        })
        }
    },
    computed: {
        
    }
}).mount('#app')