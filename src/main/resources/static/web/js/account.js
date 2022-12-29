const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            id: new URLSearchParams(location.search).get('id'),
            data: [],
            client: {},
            accounts: [],
            account: {},
            transactions: [],
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
            axios.get("/api/clients/current")
                .then(response => {
                    this.data = response.data
                    return this.data
                })
                .then(data => {
                    let USDollar = new Intl.NumberFormat("en-US",
                    {
                        style: "currency",
                        currency: "USD",
                    });
                    this.client = data;
                    this.accounts = this.client.accounts;
                    this.accounts.map(account => account.balance = USDollar.format(account.balance))
                    this.account = this.accounts.find(account => (account.id.toString()) == this.id)
                    this.transactions = this.account.transactions.map(transaction => transaction).sort((a, b) => b.id - a.id);
                    this.transactions.map(transaction => transaction.amount = USDollar.format(transaction.amount))
                })
                .catch(error => console.error(error))
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